/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.xmlcomp;

import java.util.ArrayList;
import java.util.List;

public class CompactDisc
{
    protected String title;
    protected String artist;
    protected List songs = new ArrayList();

    public CompactDisc(String title,String artist)
    {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public int getSongCount()
    {
        return songs.size();
    }

    public Song getSong(int index)
    {
        return (Song)songs.get(index);
    }

    public void insertSong(int index,Song song)
    {
        songs.add(index,song);
    }

    public void addSong(Song song)
    {
        songs.add(song);
    }

    public void removeSong(int index)
    {
        songs.remove(index);
    }

    public List getSongList()
    {
        return songs;
    }

}
