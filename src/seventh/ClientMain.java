/*
 * See license
 */
package seventh;

import java.io.File;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import seventh.client.ClientSeventhConfig;
import seventh.client.SeventhGame;
import seventh.client.gfx.VideoConfig;
import seventh.shared.Config;
import seventh.shared.Logger;
import seventh.shared.PrintStreamLogger;

/**
 * Main entry point for the client game
 * 
 * @author Tony
 *
 */
public class ClientMain {

	/**
	 * Client configuration file location
	 */
	private static final String CLIENT_CFG_PATH = "./assets/client_config.leola";

	/**
	 * Loads the client configuration file.
	 * 
	 * @return the client configuration file
	 * @throws Exception
	 */
	private static ClientSeventhConfig loadConfig() throws Exception {
		Config config = new Config(CLIENT_CFG_PATH, "client_config");
		return new ClientSeventhConfig(config);
	}

	/**
	 * Handle the exception -- piping out to a log file
	 * 
	 * @param e
	 */
	private static void catchException(Throwable e) {
		try {
			PrintStream out = new PrintStream(new File("./seventh_error.log"));
			try {
				Logger logger = new PrintStreamLogger(out);
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				logger.println("Date: " + formatter.format(new Date()));
				logSystemSpecs(logger);
				logVideoSpecs(logger);

				e.printStackTrace(out);
			} finally {
				out.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			System.exit(1);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ClientSeventhConfig config = null;
		try {

			/*
			 * LibGDX spawns another thread which we don't have access to for
			 * catching its exceptions
			 */
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread t, Throwable e) {
					if (t.getName().equals("LWJGL Application")) {
						catchException(e);
					}
				}
			});

			config = loadConfig();
			AVfacadeClient AV = new AVfacadeClient(config);
			AV.avOption();

		} catch (Exception e) {
			catchException(e);
		} finally {
			// System.exit(0);
			if (config != null) {
				// config.save(CLIENT_CFG_PATH);
			}
		}
	}

	public static void logVideoSpecs(Logger console) {
		try {
			if (Gdx.graphics != null) {
				console.println("GL30: " + Gdx.graphics.isGL30Available());
				console.println("OpenGL Version: " + Gdx.gl.glGetString(GL20.GL_VERSION));
				console.println("OpenGL Vendor: " + Gdx.gl.glGetString(GL20.GL_VENDOR));
				console.println("Renderer: " + Gdx.gl.glGetString(GL20.GL_RENDERER));
				console.println("Gdx Version: " + Gdx.app.getVersion());
				console.println("Is Fullscreen: " + Gdx.graphics.isFullscreen());
			} else {
				console.println("OpenGL Version: " + Gdx.gl.glGetString(GL20.GL_VERSION));
				console.println("OpenGL Vendor: " + Gdx.gl.glGetString(GL20.GL_VENDOR));
				console.println("Renderer: " + Gdx.gl.glGetString(GL20.GL_RENDERER));
			}
		} catch (Throwable t) {
			console.println("Error retrieving video specifications: " + t);
		}
	}

	/**
	 * Prints out system specifications
	 * 
	 * @param console
	 */
	public static void logSystemSpecs(Logger console) {
		Runtime runtime = Runtime.getRuntime();
		final long MB = 1024 * 1024;
		console.println("");
		console.println("Seventh: " + SeventhGame.getVersion());
		console.println("Available processors (cores): " + runtime.availableProcessors());
		console.println("Free memory (MiB): " + runtime.freeMemory() / MB);
		console.println("Max memory (MiB): "
				+ (runtime.maxMemory() == Long.MAX_VALUE ? "no limit" : Long.toString(runtime.maxMemory() / MB)));
		console.println("Available for JVM (MiB): " + runtime.totalMemory() / MB);

		/* Get a list of all filesystem roots on this system */
		File[] roots = File.listRoots();

		/* For each filesystem root, print some info */
		for (File root : roots) {
			console.println("File system root: " + root.getAbsolutePath());
			console.println("\tTotal space (MiB): " + root.getTotalSpace() / MB);
			console.println("\tFree space (MiB): " + root.getFreeSpace() / MB);
			console.println("\tUsable space (MiB): " + root.getUsableSpace() / MB);
		}

		console.println("Java Version: " + System.getProperty("java.version"));
		console.println("Java Vendor: " + System.getProperty("java.vendor"));
		console.println("Java VM Version: " + System.getProperty("java.vm.version"));
		console.println("Java VM Name: " + System.getProperty("java.vm.name"));
		console.println("Java Class Version: " + System.getProperty("java.class.version"));
		console.println("Java VM Spec. Version: " + System.getProperty("java.vm.specification.version"));
		console.println("Java VM Spec. Vendor: " + System.getProperty("java.vm.specification.vendor"));
		console.println("Java VM Spec. Name: " + System.getProperty("java.vm.specification.name"));

		console.println("OS: " + System.getProperty("os.name"));
		console.println("OS Arch: " + System.getProperty("os.arch"));
		console.println("OS Version: " + System.getProperty("os.version"));
		console.println("");
	}
}
