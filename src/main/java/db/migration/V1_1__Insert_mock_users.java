package db.migration;

import org.apache.commons.codec.digest.DigestUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V1_1__Insert_mock_users extends BaseJavaMigration {
	
	@Override
	public void migrate(Context context) throws Exception {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(
		        new SingleConnectionDataSource(context.getConnection(), true));

		StringBuilder stmtBuilder = new StringBuilder();
		stmtBuilder.append("INSERT INTO User VALUES (");
		stmtBuilder.append("'pepito'");
		stmtBuilder.append(", '");
		stmtBuilder.append(DigestUtils.sha256Hex("pepitom3g4h4x0r"));
		stmtBuilder.append("', ");
		stmtBuilder.append("'ROLE_USER'");
		stmtBuilder.append(");");
		
		jdbcTemplate.execute(stmtBuilder.toString());
	}
}
