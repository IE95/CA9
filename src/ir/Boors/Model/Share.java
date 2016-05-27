package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

public class Share{
	public Share(int id,int count){
		this.id = id;
		this.count = count;
	}

	public String getJson(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":");
		sb.append(id.toString());
		sb.append(",\"count\":");
		sb.append(count.toString());
		sb.append("}");
		return sb.toString();
	}
	public Integer id;
	public Integer count;
}