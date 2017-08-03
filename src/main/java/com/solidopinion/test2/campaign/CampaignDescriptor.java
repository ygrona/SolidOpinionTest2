package com.solidopinion.test2.campaign;

import java.util.Set;

/**
 * Created by Yuriy on 02.08.2017.
 *
 * Describe campaign with it segments
 */
public class CampaignDescriptor {
    private String name;
    private Set<Integer> segments;

    public CampaignDescriptor(String name, Set<Integer> segments) {
        this.name = name;
        this.segments = segments;
    }

    public String getName() {
        return name;
    }

    public Set<Integer> getSegments() {
        return segments;
    }


    @Override
    public String toString() {
        return "CampaignDescriptor{" +
                "name='" + name + '\'' +
                ", segments=" + segments +
                '}';
    }
}
