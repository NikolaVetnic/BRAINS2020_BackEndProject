package com.iktpreobuka.project.entities;

public class EmailObject {

	private String to;
	private String subject;
	private String text;
	
	public EmailObject() {
		super();
	}

	public String getTo() 		{ return to; 		}
	public String getSubject() 	{ return subject; 	}
	public String getText() 	{ return text; 		}

	public void setTo(String to) 			{ this.to = to; 			}
	public void setSubject(String subject) 	{ this.subject = subject; 	}
	public void setText(String text) 		{ this.text = text; 		}
}
