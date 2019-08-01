package fr.ustaN.main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import fr.ustaN.adds.YMLFile;
import fr.ustaN.main.SkybPvpArena;

public class CommandListener implements CommandExecutor {
	private static YMLFile configFile = SkybPvpArena.getMain().getYMLConfig();
	private static Configuration config = configFile.getConfiguration();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (arg.equals(null))
			return false;
		String text = "";
		for (int i=1;i<args.length-1;i++) {text=text+args[i]+" ";}
		text=text+args[args.length-1];
		if (args[0].equalsIgnoreCase("cancel")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if ((player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1))
						&& player.hasPermission(config.getString("permissions.autres")))
						|| player.hasPermission(config.getString("permissions.admin"))) {
					if(SkybPvpArena.getMain().getEtatDuJeu()==0)return false;
					player.sendMessage(
							SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.betCancelled")));
				} else {
					player.sendMessage(
							SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.tryBetCancelled")));
					return false;
				}
			} else
				if(SkybPvpArena.getMain().getEtatDuJeu()==0)return false;
			System.out.println(SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.betCancelled")));
			cancelPari();
		} else if (args[0].equalsIgnoreCase("sign")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission(config.getString("permissions.autres"))
						|| player.hasPermission(config.getString("permissions.admin"))) {
					player.teleport(new Location(Bukkit.getWorld(config.getString("SpawnAreas.SignTPPlace.world")),
							config.getInt("SpawnAreas.SignTPPlace.x"),config.getInt("SpawnAreas.SignTPPlace.y"),
							config.getInt("SpawnAreas.SignTPPlace.z"),config.getInt("SpawnAreas.SignTPPlace.pitch"),
							config.getInt("SpawnAreas.SignTPPlace.yaw")));
				}
			} 
			else if(!args[1].isEmpty())
			{
				if (((sender instanceof Player) && sender.hasPermission("permissions.admin")) || !(sender instanceof Player))
				{
					Player player = Bukkit.getPlayer(args[1]);
					player.teleport(new Location(Bukkit.getWorld(config.getString("SpawnAreas.SignTPPlace.world")),
						config.getInt("SpawnAreas.SignTPPlace.x"), config.getInt("SpawnAreas.SignTPPlace.y"),
						config.getInt("SpawnAreas.SignTPPlace.z"), config.getInt("SpawnAreas.SignTPPlace.pitch"),
						config.getInt("SpawnAreas.SignTPPlace.yaw")));	
				}
			}
			else
				System.out.println("commande uniquement utilisable en jeu");
		} else if (args[0].equalsIgnoreCase("clearConfig")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission(config.getString("permissions.admin")))
					player.sendMessage("uniquement disponible via la console");
				return false;
			}
			SkybPvpArena.getMain().saveResource("config.yml", true);
			configFile = new YMLFile("plugins", "BBSkyBPvpArena", "config");
			System.out.println("config reset");
		} else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission(config.getString("permissions.autres"))
						|| player.hasPermission(config.getString("permissions.admin"))) {
					String msg = "/arene help/h/? : affiche cette liste de commandes" + "__"
							+ "/arene cancel : le joueur qui a lancer le parie peut l'annuler avant que quelqu'un accepte le combat"
							+ "__" + "/arene sign : pour se TP devant le panneau";
					player.sendMessage(msg.split("__"));
				}
				if (player.hasPermission(config.getString("permissions.admin"))) {
					String msg = "§a/arene clearConfig: reset le fichier de config /!\\ c'est dangereux";
					player.sendMessage(msg);
				}
			}
		}
		return true;
	}

	public static void cancelPari() {
		SkybPvpArena.getMain().setEtatDuJeu(0);
		SkybPvpArena.getMain().getPariObj().resetPari();
		SkybPvpArena.getMain()
				.broadCast(SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.betCancelledBC")));
		SkybPvpArena.getMain().getPariObj().rendrePari(SkybPvpArena.getMain().getPlayObj().getPlayer(1), 1);
		if(SkybPvpArena.getMain().getEtatDuJeu()==4)
			SkybPvpArena.getMain().getPariObj().rendrePari(SkybPvpArena.getMain().getPlayObj().getPlayer(2), 1);
		SkybPvpArena.getMain().getPlayObj().setPlayer(null, 1);
		SkybPvpArena.getMain().getPlayObj().setPlayer(null, 2);
	}
}