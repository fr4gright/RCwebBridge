package me.rvt.rcwebbridge.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.bukkit.Bukkit.getServer;

public class Commands {

    public static void commandReader(Plugin plugin, int delay) {
        File f = new File("NextCommand.txt");
        getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (f.exists()) {
                if(f.length() > 0)
                    readFile(f);
            }
        }, 20 * delay, 20 * delay);
    }

    private static void readFile(File f) {
        try {
            //f.createNewFile();
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                String command = myReader.nextLine();
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, command);

                PrintWriter pw = new PrintWriter(f);
                pw.close();
            }
            myReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}