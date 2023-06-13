package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.Languages;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.model.Voices;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import com.ibm.watson.text_to_speech.v1.websocket.BaseSynthesizeCallback;

@Service
public class TextToSpeechService {

public void send(String message) {
	    
	    System.out.println("Message is :"+message);
	    //callMeforVoice(message);
	    //TestMe(message);
	    testTranslator(message);
	}

public void callMeforVoice(String message)
{
	Authenticator authenticator = new IamAuthenticator("rUePUBoU_3tO01gBMNL-ZBjH3amG_DiOkHUdlx0Fwpwx");
	TextToSpeech service = new TextToSpeech(authenticator);
	service.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/b7df5ce0-27a6-478f-bf28-f65f69ac0d2f");
	//String text = "It's beginning to look a lot like Christmas";
	SynthesizeOptions synthesizeOptions =
	        new SynthesizeOptions.Builder()
	            .text(message)
	            .accept("audio/ogg;codecs=opus")
	            .voice("en-US_KevinV3Voice")
	            .build();
	


	// a callback is defined to handle certain events, like an audio transmission or a timing marker
	// in this case, we'll build up a byte array of all the received bytes to build the resulting file
	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	service.synthesizeUsingWebSocket(synthesizeOptions, new BaseSynthesizeCallback() {
	  @Override
	  public void onAudioStream(byte[] bytes) {
	    // append to our byte array
	    try {
	      byteArrayOutputStream.write(bytes);
	    } catch (Throwable e) {
	      e.printStackTrace();
	    }
	  }
	});

	// quick way to wait for synthesis to complete, since synthesizeUsingWebSocket() runs asynchronously
	try {
		Thread.sleep(10);
	

	// create file with audio data
	String filename = "synthesize_websocket_test.ogg";
	OutputStream fileOutputStream = new FileOutputStream(filename);
	byteArrayOutputStream.writeTo(fileOutputStream);
	byteArrayOutputStream.flush();
	// clean up
	byteArrayOutputStream.close();
	fileOutputStream.close();
	} catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void TestTranslatedAudio(String message)

{
	Authenticator authenticator = new IamAuthenticator("rUePUBoU_3tO01gBMNL-ZBjH3amG_DiOkHUdlx0Fwpwx");

	TextToSpeech service = new TextToSpeech(authenticator);
	service.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/b7df5ce0-27a6-478f-bf28-f65f69ac0d2f");
	

	Voices voices = service.listVoices().execute().getResult();
	
	//System.out.println(voices);
	
	
	IamAuthenticator authenticator1 = new IamAuthenticator("rUePUBoU_3tO01gBMNL-ZBjH3amG_DiOkHUdlx0Fwpwx");
	TextToSpeech textToSpeech = new TextToSpeech(authenticator1);
	textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/b7df5ce0-27a6-478f-bf28-f65f69ac0d2f");

	try {
	  SynthesizeOptions synthesizeOptions =
	    new SynthesizeOptions.Builder()
	      .text(message)
	      .accept("audio/wav")
	      .voice("en-US_AllisonV3Voice")
	      .build();

	  InputStream inputStream =
	    textToSpeech.synthesize(synthesizeOptions).execute().getResult();
	  InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

	  OutputStream out = new FileOutputStream("hello_world.wav");
	  byte[] buffer = new byte[1024];
	  int length;
	  while ((length = in.read(buffer)) > 0) {
	    out.write(buffer, 0, length);
	  }

	  out.close();
	  in.close();
	  inputStream.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
	
}


public void testTranslator(String message)

{
	IamAuthenticator authenticator = new IamAuthenticator("jpLMQ3AuhQue9V9ZewBBWK_zJmPR2g4PTc-SI6kZRyfG");
	LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
	languageTranslator.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/41d57078-f8d5-4bee-a559-ea556eefb668");

	Languages languages = languageTranslator.listLanguages()
	  .execute().getResult();

	//System.out.println(languages);
	
	TranslateOptions translateOptions = new TranslateOptions.Builder()
			  .addText(message)
			  .modelId("fi-en")
			  .build();

			TranslationResult result = languageTranslator.translate(translateOptions)
			  .execute().getResult();

			String resultmessag=result.getTranslations().get(0).getTranslation();
			System.out.println(result.getTranslations().get(0).getTranslation());
			TestTranslatedAudio(resultmessag);
}

}
