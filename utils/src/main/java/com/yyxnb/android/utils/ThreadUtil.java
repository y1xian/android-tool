package com.yyxnb.android.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.yyxnb.android.ModuleManager;
import com.yyxnb.android.UtilException;

import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreadUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class ThreadUtil {

	private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

	private static final SparseArray<SparseArray<ExecutorService>> TYPE_PRIORITY_POOLS = new SparseArray<>();

	private static final Map<Task<?>, ExecutorService> TASK_POOL_MAP = new ConcurrentHashMap<>();

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

	private static final Timer TIMER = new Timer();

	private static final byte TYPE_SINGLE = -1;
	private static final byte TYPE_CACHED = -2;
	private static final byte TYPE_IO = -4;
	private static final byte TYPE_CPU = -8;

	private static Executor sDeliver;

	private ThreadUtil() {
	}

	/**
	 * 判断当前是否在UI线程
	 *
	 * @return 是否UI线程
	 */
	public static boolean isInUiThread() {
		return MAIN_HANDLER.getLooper() == Looper.myLooper();
	}

	/**
	 * 在UI线程执行任务
	 *
	 * @param runnable runnable
	 */
	public static void runOnUiThread(final Runnable runnable) {
		runOnUiThread(runnable, false);
	}

	/**
	 * 在UI线程执行任务，等待任务完成位置
	 *
	 * @param runnable        runnable
	 * @param waitUntilFinish 等待直到完成
	 */
	public static void runOnUiThread(final Runnable runnable, boolean waitUntilFinish) {
		if (isInUiThread()) {
			runnable.run();
		} else {
			FutureTask<?> futureTask = new FutureTask<>(runnable, null);
			MAIN_HANDLER.post(futureTask);
			if (waitUntilFinish) {
				getTaskResult(futureTask);
			}
		}
	}

	/**
	 * 在UI线程执行任务
	 *
	 * @param runnable    runnable
	 * @param delayMillis 延迟时间，毫秒单位
	 */
	public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
		runOnUiThreadDelayed(runnable, delayMillis, false);
	}

	/**
	 * 在UI线程执行任务
	 *
	 * @param runnable        runnable
	 * @param delayMillis     延迟时间，毫秒单位
	 * @param waitUntilFinish 等待直到完成
	 */
	public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis, boolean waitUntilFinish) {
		FutureTask<?> futureTask = new FutureTask<>(runnable, null);
		MAIN_HANDLER.postDelayed(futureTask, delayMillis);
		if (waitUntilFinish) {
			getTaskResult(futureTask);
		}
	}

	/**
	 * 在UI线程执行任务
	 *
	 * @param callable callable
	 * @return T
	 */
	public <T> T executeOnUiThread(final Callable<T> callable) {
		if (isInUiThread()) {
			try {
				return callable.call();
			} catch (Exception ex) {
				throw new UtilException(ex);
			}
		} else {
			FutureTask<T> futureTask = new FutureTask<>(callable);
			MAIN_HANDLER.post(futureTask);
			return getTaskResult(futureTask);
		}
	}

	/**
	 * 在后台线程执行任务，根据Cpu创建线程池
	 *
	 * @param runnable runnable
	 */
	public void postOnBackground(final Runnable runnable) {
		getCpuPool().execute(runnable);
	}

	/**
	 * 在后台线程执行任务，根据Cpu创建线程池
	 *
	 * @param callable callable
	 * @return T
	 */
	public <T> T executeOnBackground(Callable<T> callable) {
		FutureTask<T> futureTask = new FutureTask<>(callable);
		getCpuPool().execute(futureTask);
		return getTaskResult(futureTask);
	}

	/**
	 * 线程休眠时间
	 *
	 * @param millisecond 毫秒单位
	 */
	public static void sleepSilently(long millisecond) {
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException ex) {
			ModuleManager.log().e(ex.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 获取任务结果
	 *
	 * @param task 任务
	 * @param <T>  T
	 * @return T
	 */
	public static <T> T getTaskResult(FutureTask<T> task) {
		while (true) {
			try {
				return task.get();
			} catch (ExecutionException ex) {
				ModuleManager.log().e(ex.getMessage());
				throw new UtilException(ex);
			} catch (InterruptedException ex) {
				ModuleManager.log().e(ex.getMessage());
				Thread.currentThread().interrupt();
			} catch (CancellationException ex) {
				ModuleManager.log().e(ex.getMessage());
				return null;
			}
		}
	}

	// ----------------------------------------------- 线程池

	/**
	 * 获取固定线程池，需要时创建
	 *
	 * @param size 线程大小
	 * @return 固定线程池
	 */
	public static ExecutorService getFixedPool(@IntRange(from = 1) final int size) {
		return getPoolByTypeAndPriority(size);
	}

	public static ExecutorService getFixedPool(@IntRange(from = 1) final int size,
											   @IntRange(from = 1, to = 10) final int priority) {
		return getPoolByTypeAndPriority(size, priority);
	}

	public static <T> void executeByFixed(@IntRange(from = 1) final int size, final Task<T> task) {
		execute(getPoolByTypeAndPriority(size), task);
	}

	public static <T> void executeByFixed(@IntRange(from = 1) final int size,
										  final Task<T> task,
										  @IntRange(from = 1, to = 10) final int priority) {
		execute(getPoolByTypeAndPriority(size, priority), task);
	}

	public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) final int size,
												   final Task<T> task,
												   long initialDelay,
												   final long period,
												   final TimeUnit unit) {
		executeAtFixedRate(getPoolByTypeAndPriority(size), task, initialDelay, period, unit);
	}

	public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) final int size,
												   final Task<T> task,
												   long initialDelay,
												   final long period,
												   final TimeUnit unit,
												   @IntRange(from = 1, to = 10) final int priority) {
		executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, initialDelay, period, unit);
	}

	/**
	 * 独立线程池，需要时创建
	 *
	 * @return 线程池
	 */
	public static ExecutorService getSinglePool() {
		return getPoolByTypeAndPriority(TYPE_SINGLE);
	}

	public static ExecutorService getSinglePool(@IntRange(from = 1, to = 10) final int priority) {
		return getPoolByTypeAndPriority(TYPE_SINGLE, priority);
	}

	/**
	 * 缓存线程池，重用
	 *
	 * @return 缓存线程池
	 */
	public static ExecutorService getCachedPool() {
		return getPoolByTypeAndPriority(TYPE_CACHED);
	}

	public static ExecutorService getCachedPool(@IntRange(from = 1, to = 10) final int priority) {
		return getPoolByTypeAndPriority(TYPE_CACHED, priority);
	}

	/**
	 * IO线程池，数量为（2 * CPU_COUNT + 1）
	 *
	 * @return IO线程池
	 */
	public static ExecutorService getIoPool() {
		return getPoolByTypeAndPriority(TYPE_IO);
	}

	public static ExecutorService getIoPool(@IntRange(from = 1, to = 10) final int priority) {
		return getPoolByTypeAndPriority(TYPE_IO, priority);
	}

	/**
	 * Cpu线程池，数量为（CPU_COUNT + 1）
	 *
	 * @return Cpu线程池
	 */
	public static ExecutorService getCpuPool() {
		return getPoolByTypeAndPriority(TYPE_CPU);
	}

	public static ExecutorService getCpuPool(@IntRange(from = 1, to = 10) final int priority) {
		return getPoolByTypeAndPriority(TYPE_CPU, priority);
	}

	/**
	 * 自定义线程池
	 *
	 * @param pool ExecutorService
	 * @param task 任务
	 * @param <T>  任务结果
	 */
	public static <T> void executeByCustom(final ExecutorService pool, final Task<T> task) {
		execute(pool, task);
	}

	public static <T> void executeByCustomWithDelay(final ExecutorService pool,
													final Task<T> task,
													final long delay,
													final TimeUnit unit) {
		executeWithDelay(pool, task, delay, unit);
	}

	public static <T> void executeByCustomAtFixRate(final ExecutorService pool,
													final Task<T> task,
													long initialDelay,
													final long period,
													final TimeUnit unit) {
		executeAtFixedRate(pool, task, initialDelay, period, unit);
	}

	// ----------------------------------------------- 取消task

	public static void cancel(final Task<?> task) {
		if (task == null) {
			return;
		}
		task.cancel();
	}

	public static void cancel(final Task<?>... tasks) {
		if (tasks == null || tasks.length == 0) {
			return;
		}
		for (Task<?> task : tasks) {
			if (task == null) {
				continue;
			}
			task.cancel();
		}
	}

	public static void cancel(ExecutorService executorService) {
		if (executorService instanceof ThreadPoolExecutor4Util) {
			for (Map.Entry<Task<?>, ExecutorService> taskTaskInfoEntry : TASK_POOL_MAP.entrySet()) {
				if (taskTaskInfoEntry.getValue() == executorService) {
					cancel(taskTaskInfoEntry.getKey());
				}
			}
		} else {
			UtilInner.e("ThreadUtil", "The executorService is not ThreadUtil's pool.");
		}
	}

	// -----------------------------------------------

	private static <T> void execute(final ExecutorService pool, final Task<T> task) {
		execute(pool, task, 0, 0, null);
	}

	private static <T> void executeWithDelay(final ExecutorService pool,
											 final Task<T> task,
											 final long delay,
											 final TimeUnit unit) {
		execute(pool, task, delay, 0, unit);
	}

	private static <T> void executeAtFixedRate(final ExecutorService pool,
											   final Task<T> task,
											   long delay,
											   final long period,
											   final TimeUnit unit) {
		execute(pool, task, delay, period, unit);
	}

	private static <T> void execute(final ExecutorService pool, final Task<T> task,
									long delay, final long period, final TimeUnit unit) {
		synchronized (TASK_POOL_MAP) {
			if (TASK_POOL_MAP.get(task) != null) {
				UtilInner.e("ThreadUtils", "Task can only be executed once.");
				return;
			}
			TASK_POOL_MAP.put(task, pool);
		}
		if (period == 0) {
			if (delay == 0) {
				pool.execute(task);
			} else {
				TimerTask timerTask = new TimerTask() {
					@Override
					public void run() {
						pool.execute(task);
					}
				};
				TIMER.schedule(timerTask, unit.toMillis(delay));
			}
		} else {
			task.setSchedule(true);
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					pool.execute(task);
				}
			};
			TIMER.scheduleAtFixedRate(timerTask, unit.toMillis(delay), unit.toMillis(period));
		}
	}

	private static ExecutorService getPoolByTypeAndPriority(final int type) {
		return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
	}

	private static synchronized ExecutorService getPoolByTypeAndPriority(final int type, final int priority) {
		ExecutorService pool;
		SparseArray<ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);
		if (priorityPools == null) {
			priorityPools = new SparseArray<>();
			pool = ThreadPoolExecutor4Util.createPool(type, priority);
			priorityPools.put(priority, pool);
			TYPE_PRIORITY_POOLS.put(type, priorityPools);
		} else {
			pool = priorityPools.get(priority);
			if (pool == null) {
				pool = ThreadPoolExecutor4Util.createPool(type, priority);
				priorityPools.put(priority, pool);
			}
		}
		return pool;
	}

	static final class ThreadPoolExecutor4Util extends ThreadPoolExecutor {

		private static ExecutorService createPool(final int type, final int priority) {
			switch (type) {
				case TYPE_SINGLE:
					return new ThreadPoolExecutor4Util(1, 1,
							0L, TimeUnit.MILLISECONDS,
							new LinkedBlockingQueue4Util(),
							new UtilsThreadFactory("single", priority)
					);
				case TYPE_CACHED:
					return new ThreadPoolExecutor4Util(0, 128,
							60L, TimeUnit.SECONDS,
							new LinkedBlockingQueue4Util(true),
							new UtilsThreadFactory("cached", priority)
					);
				case TYPE_IO:
					return new ThreadPoolExecutor4Util(2 * CPU_COUNT + 1, 2 * CPU_COUNT + 1,
							30, TimeUnit.SECONDS,
							new LinkedBlockingQueue4Util(),
							new UtilsThreadFactory("io", priority)
					);
				case TYPE_CPU:
					return new ThreadPoolExecutor4Util(CPU_COUNT + 1, 2 * CPU_COUNT + 1,
							30, TimeUnit.SECONDS,
							new LinkedBlockingQueue4Util(true),
							new UtilsThreadFactory("cpu", priority)
					);
				default:
					return new ThreadPoolExecutor4Util(type, type,
							0L, TimeUnit.MILLISECONDS,
							new LinkedBlockingQueue4Util(),
							new UtilsThreadFactory("fixed(" + type + ")", priority)
					);
			}
		}

		private final AtomicInteger mSubmittedCount = new AtomicInteger();

		private LinkedBlockingQueue4Util mWorkQueue;

		ThreadPoolExecutor4Util(int corePoolSize, int maximumPoolSize,
								long keepAliveTime, TimeUnit unit,
								LinkedBlockingQueue4Util workQueue,
								ThreadFactory threadFactory) {
			super(corePoolSize, maximumPoolSize,
					keepAliveTime, unit,
					workQueue,
					threadFactory
			);
			workQueue.mPool = this;
			mWorkQueue = workQueue;
		}

		private int getSubmittedCount() {
			return mSubmittedCount.get();
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			mSubmittedCount.decrementAndGet();
			super.afterExecute(r, t);
		}

		@Override
		public void execute(@NonNull Runnable command) {
			if (this.isShutdown()) {
				return;
			}
			mSubmittedCount.incrementAndGet();
			try {
				super.execute(command);
			} catch (RejectedExecutionException ignore) {
				UtilInner.e("ThreadUtils", "This will not happen!");
				mWorkQueue.offer(command);
			} catch (Throwable t) {
				mSubmittedCount.decrementAndGet();
			}
		}
	}

	private static final class LinkedBlockingQueue4Util extends LinkedBlockingQueue<Runnable> {

		private volatile ThreadPoolExecutor4Util mPool;

		private int mCapacity = Integer.MAX_VALUE;

		LinkedBlockingQueue4Util() {
			super();
		}

		LinkedBlockingQueue4Util(boolean isAddSubThreadFirstThenAddQueue) {
			super();
			if (isAddSubThreadFirstThenAddQueue) {
				mCapacity = 0;
			}
		}

		LinkedBlockingQueue4Util(int capacity) {
			super();
			mCapacity = capacity;
		}

		@Override
		public boolean offer(@NonNull Runnable runnable) {
			if (mCapacity <= size() &&
					mPool != null && mPool.getPoolSize() < mPool.getMaximumPoolSize()) {
				// create a non-core thread
				return false;
			}
			return super.offer(runnable);
		}
	}

	static final class UtilsThreadFactory extends AtomicLong
			implements ThreadFactory {
		private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
		private static final long serialVersionUID = -9209200509960368598L;
		private final String namePrefix;
		private final int priority;
		private final boolean isDaemon;

		UtilsThreadFactory(String prefix, int priority) {
			this(prefix, priority, false);
		}

		UtilsThreadFactory(String prefix, int priority, boolean isDaemon) {
			namePrefix = prefix + "-pool-" +
					POOL_NUMBER.getAndIncrement() +
					"-thread-";
			this.priority = priority;
			this.isDaemon = isDaemon;
		}

		@Override
		public Thread newThread(@NonNull Runnable r) {
			Thread t = new Thread(r, namePrefix + getAndIncrement()) {
				@Override
				public void run() {
					try {
						super.run();
					} catch (Throwable t) {
						UtilInner.e("ThreadUtil", "Request threw uncaught throwable", t);
					}
				}
			};
			t.setDaemon(isDaemon);
			t.setUncaughtExceptionHandler((t1, e) -> System.out.println(e));
			t.setPriority(priority);
			return t;
		}
	}

	public abstract static class SimpleTask<T> extends Task<T> {

		@Override
		public void onCancel() {
			UtilInner.e("ThreadUtil", "onCancel: " + Thread.currentThread());
		}

		@Override
		public void onFail(Throwable t) {
			UtilInner.e("ThreadUtil", "onFail: ", t);
		}

	}

	public abstract static class Task<T> implements Runnable {

		private static final int NEW = 0;
		private static final int RUNNING = 1;
		private static final int EXCEPTIONAL = 2;
		private static final int COMPLETING = 3;
		private static final int CANCELLED = 4;
		private static final int INTERRUPTED = 5;
		private static final int TIMEOUT = 6;

		private final AtomicInteger state = new AtomicInteger(NEW);

		private volatile boolean isSchedule;
		private volatile Thread runner;

		private Timer mTimer;
		private long mTimeoutMillis;
		private OnTimeoutListener mTimeoutListener;

		private Executor deliver;

		public abstract T doInBackground() throws Throwable;

		public abstract void onSuccess(T result);

		public abstract void onCancel();

		public abstract void onFail(Throwable t);

		@Override
		public void run() {
			if (isSchedule) {
				if (runner == null) {
					if (!state.compareAndSet(NEW, RUNNING)) {
						return;
					}
					runner = Thread.currentThread();
					if (mTimeoutListener != null) {
						UtilInner.d("ThreadUtil", "Scheduled task doesn't support timeout.");
					}
				} else {
					if (state.get() != RUNNING) {
						return;
					}
				}
			} else {
				if (!state.compareAndSet(NEW, RUNNING)) {
					return;
				}
				runner = Thread.currentThread();
				if (mTimeoutListener != null) {
					mTimer = new Timer();
					mTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							if (!isDone() && mTimeoutListener != null) {
								timeout();
								mTimeoutListener.onTimeout();
								onDone();
							}
						}
					}, mTimeoutMillis);
				}
			}
			try {
				final T result = doInBackground();
				if (isSchedule) {
					if (state.get() != RUNNING) {
						return;
					}
					getDeliver().execute(() -> onSuccess(result));
				} else {
					if (!state.compareAndSet(RUNNING, COMPLETING)) {
						return;
					}
					getDeliver().execute(() -> {
						onSuccess(result);
						onDone();
					});
				}
			} catch (InterruptedException ignore) {
				state.compareAndSet(CANCELLED, INTERRUPTED);
			} catch (final Throwable throwable) {
				if (!state.compareAndSet(RUNNING, EXCEPTIONAL)) {
					return;
				}
				getDeliver().execute(() -> {
					onFail(throwable);
					onDone();
				});
			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Task)) {
				return false;
			}
			Task<?> task = (Task<?>) o;
			return isSchedule == task.isSchedule && mTimeoutMillis == task.mTimeoutMillis
					&& Objects.equals(state, task.state) && Objects.equals(runner, task.runner)
					&& Objects.equals(mTimer, task.mTimer)
					&& Objects.equals(mTimeoutListener, task.mTimeoutListener)
					&& Objects.equals(getDeliver(), task.getDeliver());
		}

		@Override
		public int hashCode() {
			return Objects.hash(state, isSchedule, runner, mTimer,
					mTimeoutMillis, mTimeoutListener, getDeliver());
		}

		public void cancel() {
			cancel(true);
		}

		public void cancel(boolean mayInterruptIfRunning) {
			synchronized (state) {
				if (state.get() > RUNNING) {
					return;
				}
				state.set(CANCELLED);
			}
			if (mayInterruptIfRunning) {
				if (runner != null) {
					runner.interrupt();
				}
			}

			getDeliver().execute(() -> {
				onCancel();
				onDone();
			});
		}

		private void timeout() {
			synchronized (state) {
				if (state.get() > RUNNING) {
					return;
				}
				state.set(TIMEOUT);
			}
			if (runner != null) {
				runner.interrupt();
			}
		}

		public boolean isCanceled() {
			return state.get() >= CANCELLED;
		}

		public boolean isDone() {
			return state.get() > RUNNING;
		}

		public Task<T> setDeliver(Executor deliver) {
			this.deliver = deliver;
			return this;
		}

		/**
		 * Scheduled task doesn't support timeout.
		 */
		public Task<T> setTimeout(final long timeoutMillis, final OnTimeoutListener listener) {
			mTimeoutMillis = timeoutMillis;
			mTimeoutListener = listener;
			return this;
		}

		private void setSchedule(boolean isSchedule) {
			this.isSchedule = isSchedule;
		}

		private Executor getDeliver() {
			if (deliver == null) {
				return getGlobalDeliver();
			}
			return deliver;
		}

		@CallSuper
		protected void onDone() {
			TASK_POOL_MAP.remove(this);
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
				mTimeoutListener = null;
			}
		}

		public interface OnTimeoutListener {
			void onTimeout();
		}
	}

	public static class SyncValue<T> {

		private final CountDownLatch mLatch = new CountDownLatch(1);
		private final AtomicBoolean mFlag = new AtomicBoolean();
		private T mValue;

		public void setValue(T value) {
			if (mFlag.compareAndSet(false, true)) {
				mValue = value;
				mLatch.countDown();
			}
		}

		public T getValue() {
			if (!mFlag.get()) {
				try {
					mLatch.await();
				} catch (InterruptedException e) {
					UtilInner.e(e);
				}
			}
			return mValue;
		}

		public T getValue(long timeout, TimeUnit unit, T defaultValue) {
			if (!mFlag.get()) {
				try {
					mLatch.await(timeout, unit);
				} catch (InterruptedException e) {
					UtilInner.e(e);
					return defaultValue;
				}
			}
			return mValue;
		}
	}

	private static Executor getGlobalDeliver() {
		if (sDeliver == null) {
			sDeliver = ThreadUtil::runOnUiThread;
		}
		return sDeliver;
	}

	public static Handler getMainHandler() {
		return MAIN_HANDLER;
	}

	public static void removeCallbacksAndMessages() {
		MAIN_HANDLER.removeCallbacksAndMessages(null);
	}
}
