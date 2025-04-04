/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.mp3.player.retromusic.model.lyrics;

import java.util.ArrayList;

import com.mp3.player.retromusic.model.Song;

public class Lyrics {

  private static final ArrayList<Class<? extends Lyrics>> FORMATS = new ArrayList<>();

  static {
    Lyrics.FORMATS.add(SynchronizedLyricsLRC.class);
  }

  public String data;
  public Song song;
  protected boolean parsed = false;
  protected boolean valid = false;

  /**
   * @param data = string:
   *             [00:00.00] I'm so glad you made time to see me
   *            [00:05.00] How's life? Tell me, how's your family?
   *            [00:10.00] I haven't seen them in a while
   *             ...
   * @return
   */
  public static boolean isSynchronized(String data) {
    for (Class<? extends Lyrics> format : Lyrics.FORMATS) {
      try {
        Lyrics lyrics = format.newInstance().setData(null, data);// truyền dữ liệu vào lớp định dạng
        if (lyrics.isValid()) {
          return true;// nếu hợp lệ, trả về true ngay
        }
      } catch (Exception e) {
        e.printStackTrace();// in lỗi nếu có
      }
    }
    return false;
  }

  public static Lyrics parse(Song song, String data) {
    for (Class<? extends Lyrics> format : Lyrics.FORMATS) {
      try {
        Lyrics lyrics = format.newInstance().setData(song, data);
        if (lyrics.isValid()) {
          return lyrics.parse(false);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return new Lyrics().setData(song, data).parse(false);
  }

  public String getText() {
    return this.data.trim().replaceAll("(\r?\n){3,}", "\r\n\r\n");
  }

  public boolean isSynchronized() {
    return false;
  }

  public boolean isValid() {
    this.parse(true);
    return this.valid;
  }

  public Lyrics parse(boolean check) {
    this.valid = true;
    this.parsed = true;
    return this;
  }

  public Lyrics setData(Song song, String data) {
    this.song = song;
    this.data = data;
    return this;
  }
}
