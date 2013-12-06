package es.uvigo.esei.dai.server.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.entity.XSD;

public class XSDDBDAO{
	private final Connection connection;

	public XSDDBDAO(Connection connection) {
		this.connection = connection;
	}
	public void create(XSD xsd) throws SQLException{
		String insert = "INSERT INTO XSD " +"(uuid,content) " + "VALUES (?, ?)";

		try (PreparedStatement statement = this.connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1,xsd.getUUID());
			statement.setString(2,xsd.getContent());

			if(statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar INSERT");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public void update(XSD xsd) throws PaginaNotFoundException, SQLException{

		String update = "UPDATE XSD SET uuid=?, content=?";

		try (PreparedStatement statement = this.connection.prepareStatement(update)) {
			statement.setString(1,xsd.getUUID());
			statement.setString(2,xsd.getContent());

			if(statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar UPDATE");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public void delete(String uuid) throws PaginaNotFoundException, SQLException {

		String delete = "DELETE FROM XSD WHERE uuid=?";
		try (PreparedStatement statement = this.connection.prepareStatement(delete)) {
			
			statement.setString(1, uuid);
			
			if (statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar DELETE");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public XSD get(String uuid) throws PaginaNotFoundException, SQLException {
		String select = "SELECT * FROM XSD WHERE uuid=?";
		
		try (PreparedStatement statement = this.connection.prepareStatement(select)) {
			statement.setString(1, uuid);
			try(ResultSet rs=statement.executeQuery()){
				rs.next();
				return new XSD(rs.getString("uuid"),rs.getString("content"));
			}catch(SQLException e){
				throw new PaginaNotFoundException("La pagina no existe",uuid);
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public ArrayList<String> getUUIDS() throws SQLException {
		String select = "SELECT * FROM XSD";
		
		try(PreparedStatement statement=this.connection.prepareStatement(select)){
					ResultSet rs=statement.executeQuery();
						ArrayList<String> result=new ArrayList<String>();
					    while(rs.next()){
								result.add(rs.getString("uuid"));
						}
						return result;		
			}catch (SQLException e) {
				throw e;
			}
}
	public List<XSD> list() throws SQLException{
		String select="SELECT * FROM XSD";
		List<XSD> lista=new ArrayList<XSD>();
		
		try(PreparedStatement statement=this.connection.prepareStatement(select)){
			ResultSet rs=statement.executeQuery();
			
			while(rs.next()){
				lista.add(new XSD(rs.getString("uuid"),rs.getString("content")));
			}
			return lista;
			
		}catch (SQLException e){
			throw e;
		}
	}
}
