package tdt4140.gr1816.app.api.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import graphql.schema.DataFetchingEnvironment;
import tdt4140.gr1816.app.api.CharacterRepository;
import tdt4140.gr1816.app.api.types.Droid;
import tdt4140.gr1816.app.api.types.Episode;
import tdt4140.gr1816.app.api.types.Character;
import tdt4140.gr1816.app.api.types.Human;

@Component
public class Query implements GraphQLQueryResolver {

	@Autowired
	private CharacterRepository characterRepository;

	public Character hero(Episode episode) {
		return episode != null ? characterRepository.getHeroes().get(episode)
				: characterRepository.getCharacters().get("1000");
	}

	public Human human(String id, DataFetchingEnvironment env) {
		return (Human) characterRepository.getCharacters().values().stream()
				.filter(character -> character instanceof Human && character.getId().equals(id)).findFirst()
				.orElseGet(null);
	}

	public Droid droid(String id) {
		return (Droid) characterRepository.getCharacters().values().stream()
				.filter(character -> character instanceof Droid && character.getId().equals(id)).findFirst()
				.orElseGet(null);
	}

	public Character character(String id) {
		return characterRepository.getCharacters().get(id);
	}
}
