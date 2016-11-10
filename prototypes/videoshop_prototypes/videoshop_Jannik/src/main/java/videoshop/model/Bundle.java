package videoshop.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import videoshop.model.Disc.DiscType;


public class Bundle extends Disc{
	
	private static final long serialVersionUID = 3602164805477720501L;
	
	private List<Disc> contents;
	private String genre, image;
	private DiscType type;

	
	@OneToMany(cascade = CascadeType.ALL) private List<Comment> comments = new LinkedList<Comment>();

	
	public Bundle(String name, String image, Money price, String genre, List<Disc> contents) {

		super(name, image, price, genre, DiscType.BUNDLE);

		this.image = image;
		this.contents = contents;

	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}


	public Iterable<Comment> getComments() {
		return comments;
	}

	public String getImage() {
		return image;
	}
	
	public DiscType getType() {
		return type;
	}

}
