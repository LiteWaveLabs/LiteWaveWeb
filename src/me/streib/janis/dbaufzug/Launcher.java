package me.streib.janis.dbaufzug;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import de.niklasfauth.litewave.DatabaseConnection;
import de.niklasfauth.litewave.LiteWaveMain;
import de.niklasfauth.litewave.LiteWaveWebConfigurator;
import de.niklasfauth.litewave.measure.InitSpectrometer;

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
		HandlerList hl = new HandlerList();
		hl.setHandlers(new Handler[] { generateStaticContext(), h });
		s.setHandler(hl);
		s.start();
		
		//For intel systems (OmniDriver Initialization
		/*
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			executor.submit(new InitSpectrometer()).get(1800, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e1) {
			System.out.println("Timeout: USB driver not Initialized");
			e1.printStackTrace();
		} // Timeout of 10 minutes.
		executor.shutdown(); */
		
	}

	private static Handler generateStaticContext() {
		final ResourceHandler rh = new ResourceHandler();
		rh.setResourceBase("static/");
		ContextHandler ch = new ContextHandler("/static");
		ch.setHandler(rh);
		return ch;
	}
}
