package ie.cit.jdbctemplateProject.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ie.cit.jdbctemplateProject.domain.Movement;

public class MovementRowMapper implements RowMapper<Movement> {

	@Override
	public Movement mapRow(ResultSet rs, int index) throws SQLException {
		Movement movement = new Movement();
		
		movement.setId(rs.getInt("id"));
		movement.setName(rs.getString("name"));
		
		return movement;
	}
	
}

