package com.siga.test;

/**
 * @author ASD
 *
 * Clase a Eliminar en Produccion
 */


/**
 * http://javaalmanac.com/egs/java.lang/ListThreads.html

Creating a Thread

When a thread is created, it must be permanently bound to an object with a run() method. When the thread is started, it will invoke the object's run() method. More specifically, the object must implement the Runnable interface. 
There are two ways to create a thread. The first is to declare a class that extends Thread. When the class is instantiated, the thread and object are created together and the object is automatically bound to the thread. By calling the object's start() method, the thread is started and immediately calls the object's run() method. Here is some code to demonstrate this method. 

    // This class extends Thread
    class BasicThread1 extends Thread {
        // This method is called when the thread runs
        public void run() {
        }
    }

    // Create and start the thread
    Thread thread = new BasicThread1();
    thread.start();

The second way is to create the thread and supply it an object with a run() method. This object will be permanently associated with the thread. The object's run() method will be invoked when the thread is started. This method of thread creation is useful if you want many threads sharing an object. Here is an example that creates a Runnable object and then creates a thread with the object. 

    class BasicThread2 implements Runnable {
        // This method is called when the thread runs
        public void run() {
        }
    }

    // Create the object with the run() method
    Runnable runnable = new BasicThread2();
    
    // Create the thread supplying it with the runnable object
    Thread thread = new Thread(runnable);
    
    // Start the thread
    thread.start();

Stopping a Thread

The proper way to stop a running thread is to set a variable that the thread checks occasionally. When the thread detects that the variable is set, it should return from the run() method. 
Note: Thread.suspend() and Thread.stop() provide asynchronous methods of stopping a thread. However, these methods have been deprecated because they are very unsafe. Using them often results in deadlocks and incorrect resource cleanup. 

    // Create and start the thread
    MyThread thread = new MyThread();
    thread.start();
    
    // Do work...
    
    // Stop the thread
    thread.allDone = true;
    
    class MyThread extends Thread {
        boolean allDone = false;
    
        // This method is called when the thread runs
        public void run() {
            while (true) {
                // Do work...
    
                if (allDone) {
                    return;
                }
    
                // Do work...
            }
        }
    }


Determining When a Thread Has Finished

This example demonstrates a few ways to determine whether or not a thread has returned from its run() method. 
    // Create and start a thread
    Thread thread = new MyThread();
    thread.start();
    
    // Check if the thread has finished in a non-blocking way
    if (thread.isAlive()) {
        // Thread has not finished
    } else {
        // Finished
    }
    
    // Wait for the thread to finish but don't wait longer than a
    // specified time
    long delayMillis = 5000; // 5 seconds
    try {
        thread.join(delayMillis);
    
        if (thread.isAlive()) {
            // Timeout occurred; thread has not finished
        } else {
            // Finished
        }
    } catch (InterruptedException e) {
        // Thread was interrupted
    }
    
    // Wait indefinitely for the thread to finish
    try {
        thread.join();
        // Finished
    } catch (InterruptedException e) {
        // Thread was interrupted
    }

Listing All Running Threads

A thread exists in a thread group and a thread group can contain other thread groups. This example visits all threads in all thread groups. 
    // Find the root thread group
    ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
    while (root.getParent() != null) {
        root = root.getParent();
    }
    
    // Visit each thread group
    visit(root, 0);
    
    // This method recursively visits all thread groups under `group'.
    public static void visit(ThreadGroup group, int level) {
        // Get threads in `group'
        int numThreads = group.activeCount();
        Thread[] threads = new Thread[numThreads*2];
        numThreads = group.enumerate(threads, false);
    
        // Enumerate each thread in `group'
        for (int i=0; i<numThreads; i++) {
            // Get thread
            Thread thread = threads[i];
        }
    
        // Get thread subgroups of `group'
        int numGroups = group.activeGroupCount();
        ThreadGroup[] groups = new ThreadGroup[numGroups*2];
        numGroups = group.enumerate(groups, false);
    
        // Recursively visit each subgroup
        for (int i=0; i<numGroups; i++) {
            visit(groups[i], level+1);
        }
    }

Here's an example of some thread groups that contain some threads: 
    java.lang.ThreadGroup[name=system,maxpri=10]
        Thread[Reference Handler,10,system]
        Thread[Finalizer,8,system]
        Thread[Signal Dispatcher,10,system]
        Thread[CompileThread0,10,system]
        java.lang.ThreadGroup[name=main,maxpri=10]
            Thread[main,5,main]
            Thread[Thread-1,5,main]

 * 
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class SIGATemporalThreadingAction extends Action {
	public SIGATemporalThreadingAction() {
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String result="salida";
		
		// Create the object with the run() method
		Runnable runnable = new SIGAThread();
    
		// Create the thread supplying it with the runnable object
		Thread thread = new Thread(runnable);
    
		com.atos.utils.ClsLogging.writeFileLog("Se va a lanzar el hilo ...",request, 7);
		
		com.atos.utils.ClsLogging.writeFileLog("Nombre del hilo: "+thread.getName(),request, 7);
		
		thread.setName("Hilo de ejemplo");
		
		com.atos.utils.ClsLogging.writeFileLog("Nombre del hilo: "+thread.getName(),request, 7);
		
		thread.setPriority(1);		
    
		// Start the thread
		thread.start();
		
		com.atos.utils.ClsLogging.writeFileLog("Hilo lanzado",request, 7);
				

		return mapping.findForward(result);
	}	

	/**
	 * <p>Este método es temporal, consiste en ...</p>     
	 * @param 
	 * @param 
	 */
	private void method() {
		
	}

}
