package com.solidopinion.test2.campaign;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Yuriy on 02.08.2017.
 *
 * Build {@link CampaignDescriptor} from string line.
 */
public class CampaignBuilder {
    public CampaignDescriptor buildFromString(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 2) throw new IllegalArgumentException("invalid line: " + line);
            Set<Integer> tags = new HashSet<>();
            for (int i = 1; i < parts.length; i++) {
                tags.add(Integer.parseInt(parts[i]));
            }
            return new CampaignDescriptor(parts[0], tags);
    }
}
