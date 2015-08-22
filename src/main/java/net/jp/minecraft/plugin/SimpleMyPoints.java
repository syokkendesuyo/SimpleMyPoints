package net.jp.minecraft.plugin;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 * MyHomeを設定し、コマンドでMyHomeに戻れるプラグイン
 * SimpleMyPoints v0.0.1
 * @author syokkendesuyo
 * @license creative commons 2.1 (by nc sa/表示 非営利 継承)
 */

public class SimpleMyPoints extends JavaPlugin implements Listener {

	private String version = "0.0.4 (BetaBuild)";

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// ｽﾃｲﾀｽの送信に失敗 :-(
		}

		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)){
			sender.sendMessage("Please excute this SimpleMyPoints command on a game!");
			sender.sendMessage("SimpleMyPoints コマンドはゲーム内で実行してください。");
		}
		else{
			Player player = (Player) sender;
			String displayname = player.getName();
			String uuid = player.getUniqueId().toString();
			String world =player.getLocation().getWorld().getName().toString();
			Double x =player.getLocation().getX();
			Double y =player.getLocation().getY();
			Double z =player.getLocation().getZ();
			float yaw = player.getLocation().getYaw();
			float pitch = player.getLocation().getPitch();
			String command = cmd.getLabel().toString();

			if(cmd.getName().equalsIgnoreCase("points") || cmd.getName().equalsIgnoreCase("smp")){
				if(args.length == 0){
					if(!(sender.hasPermission(HelpCommand.getPermissonNode()))){
						Messages.getPermissonError(sender, HelpCommand.getPermissonNode());
						return true;
					}
					HelpCommand.getHepMessage(sender,cmd);
					return true;
				}
				else if(args[0].equalsIgnoreCase("help")){
					if(args.length > 1){
						Messages.getArgumentTooMany(sender);
						return true;
					}
					if(!(sender.hasPermission(HelpCommand.getPermissonNode()))){
						Messages.getPermissonError(sender, HelpCommand.getPermissonNode());
						return true;
					}
					HelpCommand.getHepMessage(sender,cmd);
					return true;
				}
				else if(args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("version")){
					if(args.length > 1){
						Messages.getPermissonError(sender, VersionCommand.getPermissonNode());
						return true;
					}
					if(!(sender.hasPermission(VersionCommand.getPermissonNode()))){
						Messages.getPermissonError(sender, VersionCommand.getPermissonNode());
						return true;
					}
					VersionCommand.getVersion(sender,version);
					return true;
				}
				else if(args[0].equalsIgnoreCase("yaw")){
					if(sender.hasPermission("smp.yaw")){
						if(args.length>3){
							Messages.getArgumentTooMany(sender);
							sender.sendMessage(ChatColor.AQUA + "□ /point yaw <地点名> <方角> : 地点の視点を<方角>にセットします");
							sender.sendMessage(ChatColor.AQUA + "□ <方角> : north・south・east・west");
							return true;
						}
						if(args.length == 1){
							//points yawの実行
							sender.sendMessage(ChatColor.AQUA + "□ /point yaw <地点名> <方角> : 地点の視点を<方角>にセットします");
							sender.sendMessage(ChatColor.AQUA + "□ <方角> : north・south・east・west");
						}
						else if (args.length == 2){
							sender.sendMessage(ChatColor.RED + "□ コマンドが不正です。(理由：方角が入力されていません)");
							sender.sendMessage(ChatColor.AQUA + "□ /point yaw <地点名> <方角> : 地点の視点を<方角>にセットします");
							sender.sendMessage(ChatColor.AQUA + "□ <方角> : north・south・east・west");
						}
						else if (args.length == 3){
							String point = args[1].toString();
							FileConfiguration tf = this.getConfig();
							if(tf.getString("points." + point + ".world") == null || tf.getString("points." + point + ".x") == null || tf.getString("points." + point + ".y") == null || tf.getString("points." + point + ".z") == null){
								sender.sendMessage(ChatColor.RED + "□ " + point + "は登録されていません。");
							}
							else{
								if(args[2].equalsIgnoreCase("north")){
									setNorth(point);
									sender.sendMessage(ChatColor.AQUA + "□ " + point + "の方角を" + args[2] + "に設定しました。");
								}
								else if(args[2].equalsIgnoreCase("south")){
									setSouth(point);
									sender.sendMessage(ChatColor.AQUA + "□ " + point + "の方角を" + args[2] + "に設定しました。");
								}
								else if(args[2].equalsIgnoreCase("east")){
									setEast(point);
									sender.sendMessage(ChatColor.AQUA + "□ " + point + "の方角を" + args[2] + "に設定しました。");
								}
								else if(args[2].equalsIgnoreCase("west")){
									setWest(point);
									sender.sendMessage(ChatColor.AQUA + "□ " + point + "の方角を" + args[2] + "に設定しました。");
								}
								else{
									sender.sendMessage(ChatColor.RED + "□ " + args[3] + "という方角は存在しません。");
								}
							}
						}
						else{
							Messages.getArgumentTooMany(sender);
						}
					}
					else{
						Messages.getPermissonError(sender, "smp.yaw");
					}
				}
				else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("add")){
					if(sender.hasPermission("smp.set") || sender.hasPermission("smp.register") || sender.hasPermission("smp.add")){
						if(args.length == 1){
							sender.sendMessage(ChatColor.AQUA + "□ /" + command + " " +args[0].toString() + " <地点名> : 地点を新規で登録します");
						}
						else if(args.length == 2){
							String points = args[1].toString();
							FileConfiguration tf = this.getConfig();

							if(!(tf.getString("points." + points + ".world") == null || tf.getString("points." + points + ".x") == null || tf.getString("points." + points + ".y") == null || tf.getString("points." + points + ".z") == null)){
								sender.sendMessage(ChatColor.RED + "□ " + points  + " は既に登録されています。");
							}
							else{
								saveConfigRegister(points,displayname,uuid);
								saveConfigPos(world,points,x,y,z,yaw,pitch);

								//座標は四捨五入で表示する
								sender.sendMessage(ChatColor.AQUA + "□ 現在地を " + points  + " と定めました。[ "+ world + " , " + Math.round(x) + " , " + Math.round(y) + " , " + Math.round(z) + " ]");
								sender.sendMessage(ChatColor.AQUA + "□ /warp " + points  + " : 実行すると現在設定した場所へテレポート可能です。");
							}
						}
						else{
							Messages.getArgumentTooMany(sender);
						}
					}
					else{
						Messages.getPermissonError(sender, "smp.set/smp.register/smp.add");
					}
				}
				//ワープコマンド /points warp <地点>
				else if(args[0].equalsIgnoreCase("warp") || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")){
					if(args.length == 1){
						if(sender.hasPermission("smp.warp.spawn")){
							String point = "*warp*";
							teleport(point,sender);
						}
					}
					else if(args.length == 2){
						String point = args[1].toString();
						try{
							if(sender.hasPermission("smp.warp." + point)){
								teleport(point,sender);
							}
						}
						catch(NullPointerException ex){//データ破損が確認された場合の処理
							sender.sendMessage(ChatColor.RED + "□ "+ point + " へテレポートできませんでした。(理由：データ破損)");
						}
					}
					else{
						Messages.getArgumentTooMany(sender);
					}
				}
				//地点の詳細を表示するコマンド
				else if(args[0].equalsIgnoreCase("detail")){
					if(sender.hasPermission("smp.detail")){
						if(args.length == 1){
							sender.sendMessage(ChatColor.AQUA + "□ /" + command + " detail <地点名> : 登録地点の詳細を確認します");
						}
						else if(args.length == 2){
							String point = args[1].toString();
							detail(point , sender);
						}
						else{
							Messages.getArgumentTooMany(sender);
						}
					}
					else{
						Messages.getPermissonError(sender, "smp.detail");
					}
				}
				//削除するコマンド
				else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")){
					if(sender.hasPermission("smp.delete") || sender.hasPermission("smp.remove")){
						if(args.length == 1){
							sender.sendMessage(ChatColor.AQUA + "□ /"+ command + " " + args[0].toString() + " <地点名> : 登録地点を削除します");
						}
						else if(args.length == 2){
							String point = args[1].toString();
							deletepoint(point , sender);
						}
						else{
							Messages.getArgumentTooMany(sender);
						}
					}
					else{
						Messages.getPermissonError(sender, "smp.remove/smp.delete");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "□ コマンドが不正です。(理由：コマンドが存在しません)");
				}
			}
			//ワープコマンド
			else if (cmd.getName().equalsIgnoreCase("warp")){
				if(args.length == 0){
					String point = "*warp*";
					if(sender.hasPermission("smp.warp." + point)){
						try{
							teleport(point,sender);
						}
						catch(NullPointerException ex){//データ破損が確認された場合の処理
							sender.sendMessage(ChatColor.RED + "□ "+ point + " へテレポートできませんでした。(理由：データ破損)");
						}
					}
					else{
						sender.sendMessage(ChatColor.RED + "□ パーミッションがありません。(smp.warp." + point + ")");
					}
				}
				else if(args.length == 1){
					String point = args[0].toString();
					if(sender.hasPermission("smp.warp." + point)){
						try{
							teleport(point,sender);
						}
						catch(NullPointerException ex){//データ破損が確認された場合の処理
							sender.sendMessage(ChatColor.RED + "□ "+ point + " へテレポートできませんでした。(理由：データ破損)");
						}
					}
					else{
						sender.sendMessage(ChatColor.RED + "□ パーミッションがありません。(smp.warp." + point + ")");
					}
				}
				else{
					Messages.getArgumentTooMany(sender);
				}
			}
		}
		return true;
	}

	//看板右クリックでテレポート
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
        }
		else{
			if(block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)){
				Sign sign = (Sign) block.getState();
				if (sign.getLine(0).equalsIgnoreCase("[Teleport]") && player.hasPermission("smp.sign.warp." + sign.getLine(1))){
					String tpto = sign.getLine(1);
					if(sign.getLine(1).equalsIgnoreCase("")){
						player.sendMessage(ChatColor.RED + "□ テレポートできませんでした。(理由：看板の2行目に記載がありません)");
						return;
					}
					try{
						teleport(tpto,player);
					}
					catch(NullPointerException ex){//データ破損が確認された場合の処理
						player.sendMessage(ChatColor.RED + "□ "+ tpto + " へテレポートできませんでした。(理由：データ破損)");
					}
				}
			}
		}
	}

	//データをconfigに保存する関数(座標・視点)
	public void saveConfigPos(String world ,String points ,Double x , Double y , Double z , float yaw , float pitch){
		this.getConfig().set("points." + points + ".world" , world);
		this.getConfig().set("points." + points + ".x" , x);
		this.getConfig().set("points." + points + ".y" , y);
		this.getConfig().set("points." + points + ".z" , z);
		this.getConfig().set("points." + points + ".yaw" , yaw);
		this.getConfig().set("points." + points + ".pitch" , pitch);
		saveConfig();
	}

	//データをconfigに保存する関数(保存者名・UUID)
	public void saveConfigRegister(String points , String name , String uuid){
		this.getConfig().set("points." + points + ".registrant_name" , name);
		this.getConfig().set("points." + points + ".registrant_uuid" , uuid);
		saveConfig();
	}

	//削除
	public void deletepoint(String point , CommandSender sender){
		FileConfiguration tf = this.getConfig();
		if(tf.getString("points." + point + ".world") == null || tf.getString("points." + point + ".x") == null || tf.getString("points." + point + ".y") == null || tf.getString("points." + point + ".z") == null){
			sender.sendMessage(ChatColor.RED + "□ " + point + "は登録されていないため削除できませんでした。");
		}
		else{
			this.getConfig().set("points." + point + ".registrant_name" , null);
			this.getConfig().set("points." + point + "..registrant_uuid" , null);
			this.getConfig().set("points." + point + ".world" , null);
			this.getConfig().set("points." + point + ".x" , null);
			this.getConfig().set("points." + point + ".y" , null);
			this.getConfig().set("points." + point + ".z" , null);
			this.getConfig().set("points." + point + ".yaw" , null);
			this.getConfig().set("points." + point + ".pitch" , null);
			this.getConfig().set("points." + point ,null);
			saveConfig();

			sender.sendMessage(ChatColor.AQUA + "□ 登録地点 " + point + " を削除しました。");
		}
	}

	//視点を北にする関数
	public void setNorth(String points){
		this.getConfig().set("points." + points + ".yaw" , -180);
		this.getConfig().set("points." + points + ".pitch" , 0);
		saveConfig();
	}

	//視点を南にする関数
	public void setSouth(String points){
		this.getConfig().set("points." + points + ".yaw" , 0);
		this.getConfig().set("points." + points + ".pitch" , 0);
		saveConfig();
	}

	//視点を西にする関数
	public void setWest(String points){
		this.getConfig().set("points." + points + ".yaw" , 90);
		this.getConfig().set("points." + points + ".pitch" , 0);
		saveConfig();
	}

	//視点を東にする関数
	public void setEast(String points){
		this.getConfig().set("points." + points + ".yaw" , -90);
		this.getConfig().set("points." + points + ".pitch" , 0);
		saveConfig();
	}

	//テレポートする関数
	public void teleport(String point , CommandSender sender){

		FileConfiguration tf = this.getConfig();

		if(tf.getString("points." + point + ".world") == null || tf.getString("points." + point + ".x") == null || tf.getString("points." + point + ".y") == null || tf.getString("points." + point + ".z") == null){
			Messages.getNotingPoint(sender, point);
		}
		else if(sender.hasPermission("smp.use." + point)){
			String getworld = this.getConfig().getString("points." + point + ".world");
			Double getx = this.getConfig().getDouble("points." + point + ".x");
			Double gety = this.getConfig().getDouble("points." + point + ".y");
			Double getz = this.getConfig().getDouble("points." + point + ".z");
			float getyaw = (float) this.getConfig().getDouble("points." + point + ".yaw");
			float getpitch = (float) this.getConfig().getDouble("points." + point + ".pitch");

			//configから抽出したワールド名をbukkit仕様の"World"変数setworldへ代入
			//この処理が無いとワールドを取得できません。
			World setworld = Bukkit.getWorld(getworld);

			Location location = new Location(setworld, getx, gety, getz);
			location.setYaw(getyaw);
			location.setPitch(getpitch);
			Player player = (Player) sender;
			player.teleport(location);

			sender.sendMessage(ChatColor.AQUA + "□ 登録地点 " + point + " にテレポートしました。");
		}
		else{
			sender.sendMessage(ChatColor.RED + "□ "+ point + " へは権限が無いためテレポートできません。");
		}
	}

	//登録地点の詳細を表示
	public void detail(String point , CommandSender sender){
		FileConfiguration tf = this.getConfig();

		if(tf.getString("points." + point + ".world") == null || tf.getString("points." + point + ".x") == null || tf.getString("points." + point + ".y") == null || tf.getString("points." + point + ".z") == null){
			sender.sendMessage(ChatColor.RED + "□ "+ point + "は登録されていないため詳細を表示できませんでした。");
		}
		else{
			String getworld = this.getConfig().getString("points." + point + ".world");
			String getname = this.getConfig().getString("points." + point + ".registrant_name");
			Double getx = this.getConfig().getDouble("points." + point + ".x");
			Double gety = this.getConfig().getDouble("points." + point + ".y");
			Double getz = this.getConfig().getDouble("points." + point + ".z");

			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.GOLD + "--------  登録地点：" + point + " --------");
			sender.sendMessage(ChatColor.AQUA + "登録者名  ： " + ChatColor.WHITE + getname);
			sender.sendMessage(ChatColor.AQUA + "ワールド名： " + ChatColor.WHITE + getworld);
			sender.sendMessage(ChatColor.AQUA + "座標      ： " + ChatColor.WHITE + Math.round(getx) + " , " + Math.round(gety) + " , " + Math.round(getz));
			sender.sendMessage(" ");
		}
	}

}
