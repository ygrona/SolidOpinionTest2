package com.solidopinion.test2.campaign;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 02.08.2017.
 */
public class CampaignSelectorTest {


    @Test
    public void performanceTest() throws IOException, URISyntaxException {
        URL campaignURL= ClassLoader.getSystemResource("./campaign.txt");
        Path p = Paths.get(campaignURL.toURI());
        URL inputURL= ClassLoader.getSystemResource("./input.txt");
        List<String> inp = Files.readAllLines(Paths.get(inputURL.toURI()));
        inp = inp.stream().map(line->line.substring(line.indexOf(" ") + 1)).collect(Collectors.toList());
        CampaignSelector campaignSelector = new CampaignSelector();
        campaignSelector.loadFromFile(p);
        String[] res = new String[inp.size()];
        long t = -System.currentTimeMillis();
        int i = 0;
        for (String inLine : inp) {
            res[i++] = campaignSelector.select(inLine);
        }
        t += System.currentTimeMillis();
        for (i = 0 ;i < inp.size();i++) {
            System.out.println("" + i + " : " + res[i]);
        }
        System.out.println("time  :  " + t);

    }

    @Test
    public void baseTest() {
        Map<String, List<String>> inToOut = new HashMap<>();
        addInToOut(inToOut, "3 4 5 10 2 200", "campaign_a");
        addInToOut(inToOut, "3", "campaign_a","campaign_c","campaign_b");
        addInToOut(inToOut, "4 10 15", "campaign_a");
        addInToOut(inToOut, "1024 15 200 21 9 14 15", "campaign_b");
        addInToOut(inToOut, "9000 29833 65000", null);


        CampaignSelector campaignSelector = new CampaignSelector();
        campaignSelector.loadFfromStrings(Arrays.asList(new String[]{
                "campaign_a 3 4 10 2",
                "campaign_b 9 14 15 21 3",
                "campaign_c 12 1024 200 3 9 4"
        }));
        for (Map.Entry<String, List<String>> entry : inToOut.entrySet()) {
            String out = campaignSelector.select(entry.getKey());
            assertTrue("not found right expected out '"
                    + out + "' for in '"
                    + entry.getKey() + "'", entry.getValue().contains(out));

        }
    }

    @Test
    public void duplicateInputTest() {
        Map<String, List<String>> inToOut = new HashMap<>();
        addInToOut(inToOut, "3 4 5 10 2 200 200 200 200", "campaign_a");


        CampaignSelector campaignSelector = new CampaignSelector();
        campaignSelector.loadFfromStrings(Arrays.asList(new String[]{
                "campaign_a 3 4 10 2",
                "campaign_b 9 14 15 21 3",
                "campaign_c 12 1024 200 3 9 4"
        }));
        for (Map.Entry<String, List<String>> entry : inToOut.entrySet()) {
            String out = campaignSelector.select(entry.getKey());
            assertTrue("not found right expected out '"
                    + out + "' for in '"
                    + entry.getKey() + "'", entry.getValue().contains(out));

        }
    }

    private void checkOut(String in, List<String> outs, String out) {
        assertTrue("not found right expected out '" + out + "' for in '" + in + "'", outs.contains(out));
    }

    private void addInToOut(Map<String, List<String>> inToOut, String in, String... outs) {
        if (outs == null) {
            outs = new String[]{null};
        }
        inToOut.put(in, Arrays.asList(outs));
    }
}
