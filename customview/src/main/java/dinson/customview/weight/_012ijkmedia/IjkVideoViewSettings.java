/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dinson.customview.weight._012ijkmedia;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class IjkVideoViewSettings {
    private Context mAppContext;
    private SharedPreferences mSharedPreferences;

    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    //public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public IjkVideoViewSettings(Context context) {
        mAppContext = context.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAppContext);
    }

    public boolean getEnableBackgroundPlay() {
        return mSharedPreferences.getBoolean("pref.enable_background_play", false);
    }

    public int getPlayer() {
        String value = mSharedPreferences.getString("pref.player", "");
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getUsingMediaCodec() {
        return mSharedPreferences.getBoolean("pref.using_media_codec", false);
    }

    public boolean getUsingMediaCodecAutoRotate() {
        return mSharedPreferences.getBoolean("pref.key_using_media_codec_auto_rotate", false);
    }

    public boolean getMediaCodecHandleResolutionChange() {
        return mSharedPreferences.getBoolean("pref.key_media_codec_handle_resolution_change", false);
    }

    public boolean getUsingOpenSLES() {
        return mSharedPreferences.getBoolean("pref_key_using_opensl_es", false);
    }

    public String getPixelFormat() {
        return mSharedPreferences.getString("pref_key_pixel_format", "");
    }

    public boolean getEnableNoView() {
        return mSharedPreferences.getBoolean("pref_key_enable_no_view", false);
    }

    public boolean getEnableSurfaceView() {
        return mSharedPreferences.getBoolean("pref_key_enable_surface_view", false);
    }

    public boolean getEnableTextureView() {
        return mSharedPreferences.getBoolean("pref_key_enable_texture_view", false);
    }

    public boolean getEnableDetachedSurfaceTextureView() {
        return mSharedPreferences.getBoolean("pref_key_enable_detached_surface_texture", false);
    }

    public boolean getUsingMediaDataSource() {
        return mSharedPreferences.getBoolean("pref_key_using_mediadatasource", false);
    }

    public String getLastDirectory() {
        return mSharedPreferences.getString("pref_key_last_directory", "/");
    }

    public void setLastDirectory(String path) {
        mSharedPreferences.edit().putString("pref_key_last_directory", path).apply();
    }
}
