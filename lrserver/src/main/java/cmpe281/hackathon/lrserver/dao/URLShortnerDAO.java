package cmpe281.hackathon.lrserver.dao;

public interface URLShortnerDAO {
	
	public String getOriginalURLByShortenURL(String shortenURL);
}
