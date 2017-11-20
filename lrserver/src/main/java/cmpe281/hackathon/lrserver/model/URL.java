package cmpe281.hackathon.lrserver.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urls")
public class URL {

	private String slug;
	private String destination;
	
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	
}
