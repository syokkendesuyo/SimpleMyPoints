package net.jp.minecraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class HelpCommand extends SimpleMyPoints {

	private static final String COMMAND_NAME = "help";
	private static final String PERMISSON_NODE = "smp."+COMMAND_NAME;

	/*
	 *  helpコマンドの実行クラス
	 * @auther syokkendesuyo
	 */

	public static String getCommandName(){
		return COMMAND_NAME;
	}

	public static String getPermissonNode(){
		return PERMISSON_NODE;
	}

	public static void getHepMessage(CommandSender sender , Command cmd){
		String command = cmd.getLabel().toString();
		Messages.getTitle(sender);
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" help : ヘルプを表示");
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" version : バージョンを表示");
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" warp <地点名> : 登録地点へテレポート");
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" add <地点名> : 登録地点を追加");
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" detail <地点名> : 登録地点の詳細を表示");
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" delete <地点名> : 登録地点を削除");
		sender.sendMessage(ChatColor.AQUA + " /"+ command +" <地点名> <方角> : 登録地点の方角をセット");
		sender.sendMessage(ChatColor.AQUA + " /warp <地点名> : 登録地点へテレポート");
		Messages.getLine(sender);
		Messages.getVoidLine(sender);
	}
}
