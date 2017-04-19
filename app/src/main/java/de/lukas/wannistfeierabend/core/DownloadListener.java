package de.lukas.wannistfeierabend.core;

/**
 * Created by Lukas on 19.04.2017.
 */

public interface DownloadListener {

    void onFinishedSuccess(final String newVersion);
    void onFinishedFailure();
}
