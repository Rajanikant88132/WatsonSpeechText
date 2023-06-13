package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TextToSpeechService;

@RestController
@RequestMapping(value = "/ibm-watson-TextToSpeech/")
public class TextToSpeechController {
	
	@Autowired
	TextToSpeechService textToSpeechService;
	
	@GetMapping(value = "/Text2Speech")
	public String producer(@RequestParam("message") String message) {
		textToSpeechService.send(message);

		return "Message sent to the IBM Watson Assistance Successfully";
	}

}
