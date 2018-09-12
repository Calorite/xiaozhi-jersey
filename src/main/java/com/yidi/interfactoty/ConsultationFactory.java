package com.yidi.interfactoty;

public interface ConsultationFactory {
	String text(String text);
	String sendpicture(String uri);
	String sendvoice(String uri);
}
