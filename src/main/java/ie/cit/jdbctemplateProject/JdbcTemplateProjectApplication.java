package ie.cit.jdbctemplateProject;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import ie.cit.jdbctemplateProject.domain.Artist;
import ie.cit.jdbctemplateProject.domain.Movement;
import ie.cit.jdbctemplateProject.rowmapper.ArtistRowMapper;
import ie.cit.jdbctemplateProject.rowmapper.MovementRowMapper;

@SpringBootApplication
public class JdbcTemplateProjectApplication implements CommandLineRunner {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(JdbcTemplateProjectApplication.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		query01();
		query02();
		query03();
		query04();
		query05();
	}
	
	public void query01() {
		// Query for a list of maps with key-value pairs
		// The hard way!!!
			
		System.out.println("\nQuery 1 (List all artists using resultset Map)\n----------------");
			
		String sql = "SELECT * FROM artists";
		List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
			
		for (Map<String, Object> row : resultSet) {
			System.out.println("Name: " + row.get("fullName"));
			System.out.println("ID: " + row.get("id"));
			System.out.println("Gender: " + row.get("gender") + "\n");
		}
	}

	public void query02() {
		// Query for a list of objects - automatic mapping from row to object using RowMapper class
		// Using parameterised "prepared statements" reduces the risk of a SQL inject attack
			
		System.out.println("\nQuery 2 (List male artists using RowMapper)\n-----------------");
			
		String sql = "SELECT * FROM artists WHERE gender = ?";
		List<Artist> artists = jdbcTemplate.query(sql, new Object[] { "male" }, new ArtistRowMapper());
			
		for (Artist artist : artists) {
			System.out.println(artist.toString());
		}
	}
	
	public void query03() {
		// Query for a specific object - automatic mapping from row to object using RowMapper class
			
		System.out.println("\nQuery 3 (Print artist with id 1 - uses RowMapper)\n------------------");
			
		String sql = "SELECT * FROM artists WHERE id = ?";
		Artist artist = jdbcTemplate.queryForObject(sql, new Object[] { 1 }, new ArtistRowMapper());
			
		System.out.println(artist.toString());
	}


	public void query04() {
		// Query for specific column values
			
		System.out.println("\nQuery 4 (Specfic columns)\n------------------");
			
		// Old version (now deprecated)
//		String sql = "SELECT count(*) FROM artists";
//		int artistCount = jdbcTemplate.queryForInt(sql);
//		System.out.printf("Number of artists: %d\n", artistCount);
			
		// New way
		String sql = "SELECT count(*) FROM movements";
		int movementCount = jdbcTemplate.queryForObject(sql, Integer.class);
		System.out.printf("Number of movements: %d\n", movementCount);
			
		// Getting a map of values
		sql = "SELECT fullName, gender FROM artists WHERE id = 1";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		System.out.printf("Name: %s, Gender: %s\n", map.get("fullName"), 
						map.get("gender"));
	}
	
	public void query05() {
		// Specific artist and the artist's movements (many-to-many)
		// This is an inefficient version with 2 separate queries

		System.out.println("\nQuery 4 (Print artist and movements - 2 queries)\n------------------");

		String sql = "SELECT * FROM artists WHERE id = ?";
		Artist artist = jdbcTemplate.queryForObject(sql, new Object[] { 1 }, new ArtistRowMapper());

		sql = "SELECT m.* FROM movements m, artist_movements am WHERE m.id = am.movement_id AND am.artist_id = ?";
		List<Movement> movements = jdbcTemplate.query(sql, new Object[] { 1 }, new MovementRowMapper());
			
		artist.setMovements(movements);
		
		System.out.println(artist.toString());
	}





}
