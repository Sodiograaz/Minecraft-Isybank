package dev.sodiograaz.internal;

import dev.sodiograaz.commands.CommandManager;
import dev.sodiograaz.storage.StorageManager;
import lombok.Getter;
import lombok.SneakyThrows;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public final class IsybankEconomy extends JavaPlugin {
	
	private static @Getter IsybankEconomy instance;
	private static BukkitLibraryManager libraryManager;
	private static @Getter StorageManager storageManager;
	
	private static Library[] librariesToDownload = {
			Library.builder()
					.groupId("com{}zaxxer")
					.artifactId("HikariCP")
					.version("5.1.0")
					.build(),
			Library.builder()
					.groupId("org{}mybatis")
					.artifactId("mybatis")
					.version("3.5.16")
					.build(),
			Library.builder()
					.groupId("org{}reflections")
					.artifactId("reflections")
					.version("0.10.2")
					.build(),
			Library.builder()
					.groupId("org{}mariadb{}jdbc")
					.artifactId("mariadb-java-client")
					.version("3.3.3")
					.build()
	};
	
	@Override
	public void onEnable()
	{
		saveDefaultConfig();
		instance = this;
		libraryManager = new BukkitLibraryManager(this);
		libraryManager.addMavenCentral();
		libraryManager.addJitPack();
		libraryManager.addSonatype();
		for(var library : librariesToDownload)
		{
			libraryManager.downloadLibrary(library);
			libraryManager.loadLibrary(library);
		}
		storageManager = new StorageManager(this)
				.createConnection();
		
		CommandManager.reflect();
		lookupListeners();
	}
	
	@Override
	public void onDisable()
	{
	
	}
	
	@SneakyThrows
	private void lookupListeners()
	{
		for(var clazz : new Reflections("dev.sodiograaz.listeners.listeners")
				.getSubTypesOf(Listener.class))
		{
			Listener listener = clazz.getConstructor().newInstance();
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	
}