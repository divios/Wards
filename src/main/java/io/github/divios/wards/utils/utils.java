package io.github.divios.wards.utils;

import io.github.divios.core_lib.misc.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.UUID;

public class utils {

    public static boolean isEmpty(File file) {

        FileReader fr = null;   //reads the file
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            return true;
        }

        BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
        StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
        String line;

        int lines = 0;

        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
                lines++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines == 0;
    }


}
