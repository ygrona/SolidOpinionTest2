package com.solidopinion.test2;

import com.solidopinion.test2.campaign.CampaignSelector;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Yuriy on 02.08.2017.
 */
public class Main {
    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.err.println("Please provide campaigns description file as parameter");
        }

        CampaignSelector campaignSelector = new CampaignSelector();
        campaignSelector.loadFromFile(Paths.get(args[0]));

        Scanner sc = new Scanner(System.in);
        while(true) {
            String inLine = sc.nextLine();
            try {
                String out = campaignSelector.select(inLine);
                if (out != null) {
                    System.out.println(out);
                } else {
                    System.out.println("no campaign");
                }
            } catch (NumberFormatException e) {
                System.err.println("type numbers only.");
            }
        }
    }
}
