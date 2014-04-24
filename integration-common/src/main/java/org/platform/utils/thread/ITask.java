package org.platform.utils.thread;

public interface ITask<T> {
	
	public T produce();
	
	public void consume(T t);

}
