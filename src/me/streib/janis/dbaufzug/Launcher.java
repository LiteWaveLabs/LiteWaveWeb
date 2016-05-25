package me.streib.janis.dbaufzug;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import de.niklasfauth.litewave.DatabaseConnection;
import de.niklasfauth.litewave.LiteWaveMain;
import de.niklasfauth.litewave.LiteWaveWebConfigurator;
import de.niklasfauth.litewave.measure.InitSpectrometer;
import de.niklasfauth.litewave.utils.HardwareInterface;
import de.niklasfauth.litewave.utils.JSONupdater;
import de.niklasfauth.litewave.utils.QueueSharer;

public class Launcher {
	public static void main(String[] args) throws Exception {
		
		LiteWaveWebConfigurator conf;
		if (args.length != 1) {
			InputStream ins;
			File confFile = new File("conf/litewave.properties");

			ins = new FileInputStream(confFile);
			conf = new LiteWaveWebConfigurator(ins);
		} else {
			conf = new LiteWaveWebConfigurator(new FileInputStream(new File(
					args[0])));
		}
		

		DatabaseConnection.init(conf);
		
		Server s = new Server(new InetSocketAddress(conf.getHostName(),
				conf.getPort()));
		System.out.println("start Server");
		((QueuedThreadPool) s.getThreadPool()).setMaxThreads(20);
		ServletContextHandler h = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		h.setInitParameter(SessionManager.__SessionCookieProperty, "DB-Session");
		h.addServlet(LiteWaveMain.class, "/*");
		
		 // add special pathspec of "/home/" content mapped to the homePath
        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
        holderHome.setInitParameter("resourceBase","data");
        holderHome.setInitParameter("dirAllowed","true");
        holderHome.setInitParameter("pathInfoOnly","true");
        h.addServlet(holderHome,"/data/*");
		
		HandlerList hl = new HandlerList();
		hl.setHandlers(new Handler[] { generateStaticContext(), h });
		s.setHandler(hl);
		s.start();
	       
	    
	    
	    LinkedBlockingQueue queue = new LinkedBlockingQueue(10);
	    new QueueSharer(queue);
	    JSONupdater producer = new JSONupdater(QueueSharer.getQueue());
        HardwareInterface obsConsumer = new HardwareInterface(QueueSharer.getQueue());
		
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(obsConsumer);
        consumerThread.start();
	    producerThread.start();
		//For intel systems (OmniDriver Initialization
		
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			executor.submit(new InitSpectrometer()).get(1800, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e1) {
			System.out.println("Timeout: USB driver not Initialized");
			e1.printStackTrace();
		} // Timeout of 10 minutes.
		executor.shutdown(); 
		
	}

	private static Handler generateStaticContext() {
		final ResourceHandler rh = new ResourceHandler();
		rh.setResourceBase("static/");
		ContextHandler ch = new ContextHandler("/static");
		ch.setHandler(rh);
		return ch;
	}
}
