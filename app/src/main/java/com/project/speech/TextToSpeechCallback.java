package com.project.speech;

public interface TextToSpeechCallback  {
    void onStart();
    void onCompleted();
    void onError();
}
