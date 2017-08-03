package com.solidopinion.test2.campaign;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by Yuriy on 02.08.2017.
 *
 * Facade for selector framework which load campaigns and do select from input
 */


public class CampaignSelector {
    private Map<Integer, Set<CampaignDescriptor>> segmentsToDescriptors =
            Collections.synchronizedMap(new HashMap<>());

    /**
     * Loading campaigns from file
     * @param path input file path
     * @throws IOException in case of problems with file reading.
     */
    public void loadFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        loadFfromStrings(lines);
    }

    /**
     * Loading campaigns from string lines.
     * @param lines campaigns descriptors by string lines.
     *              Each line have to consists of campaign name and it segments separated with space.
     */
    public void loadFfromStrings(List<String> lines) {
        CampaignBuilder campaignBuilder = new CampaignBuilder();
        lines.parallelStream()
                .map(line->campaignBuilder.buildFromString(line))
                .forEach(desc->
                        desc.getSegments().stream().forEach(tag->{
                            Set<CampaignDescriptor> tagDescs;
                            synchronized (segmentsToDescriptors) {
                                tagDescs = segmentsToDescriptors.get(tag);
                                if (tagDescs == null) {
                                    tagDescs = Collections.synchronizedSet(new HashSet<>());
                                    segmentsToDescriptors.put(tag, tagDescs);
                                }
                            }
                            tagDescs.add(desc);
                        })
                );
    }

    /**
     * Do select campaign from input by max matching segments.
     * @param inputLine line of segments separated by space.
     * @return selected campaign or null if no one matched.
     */
    public String select(String inputLine) {
        Set<String> userSegments = new HashSet<>(Arrays.asList(inputLine.split(" ")));
        Map<CampaignDescriptor, Integer> campaignToMatchCount = new HashMap<>(20);
        CampaignMatcher maxMatcher = new CampaignMatcher();
        for (String userSegment: userSegments) {
            Set<CampaignDescriptor> campaignDescriptors = segmentsToDescriptors.get(Integer.parseInt(userSegment));
            if (campaignDescriptors != null) {
                campaignDescriptors.stream().forEach(checkingDescriptor->{
                    Integer currentMatchCount = campaignToMatchCount.get(checkingDescriptor);
                    if (currentMatchCount == null) {
                        currentMatchCount = 1;
                    } else {
                        currentMatchCount++;
                    }
                    campaignToMatchCount.put(checkingDescriptor, currentMatchCount);
                    maxMatcher.match(currentMatchCount, checkingDescriptor);
                });
            }
        }
        if (maxMatcher.descriptor != null) {
            return maxMatcher.descriptor.getName();
        } else {
            return null;
        }
    }


    private static class CampaignMatcher {
        public CampaignDescriptor descriptor = null;
        public int count = 0;
        public void match(int newCount, CampaignDescriptor newDescriptor) {
            if (newCount > count) {
                count = newCount;
                descriptor = newDescriptor;
            }
        }
    }
}
