package com.atguigu.springcloud.other.jsqlparser;

import com.atguigu.springcloud.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.AddAliasesVisitor;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.validation.Validation;
import net.sf.jsqlparser.util.validation.ValidationError;
import net.sf.jsqlparser.util.validation.feature.DatabaseType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseSQL {

    public static void validate() {
        String sql = "ALTER TABLE mytable ADD COLUMN price numeric(10,5) not null";
        // String sql = "DROP INDEX IF EXISTS idx_tab2_id;";
        //String sql = "ALTER TABLE `wf_position` ADD COLUMN `c1` VARCHAR (10);" +
        //        "ALTER TABLE `wf_position` ADD COLUNM `c2` VARCHAR (10)";
        // validate statement if it's valid for all given databases.
        Validation validation = new Validation(Collections.singletonList(DatabaseType.MYSQL), sql);
        List<ValidationError> errors = validation.validate();
        System.out.println(errors);
    }

    public static void checkAllItem() throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse("SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col1 = 10 AND col2 = 20 AND col3 = 30");

        Map<String, Expression> map = new HashMap<>();
        for (SelectItem selectItem : ((PlainSelect) stmt.getSelectBody()).getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    map.put(item.getAlias().getName(), item.getExpression());
                }
            });
        }

        log.info("map " + map);
    }

    public static void addAlias() throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse("select a,b,c from test");
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        select.getSelectBody().accept(instance);

        log.info(select.toString());
    }

    public static void highRisk(String sql) throws JSQLParserException {
        Statements statements = CCJSqlParserUtil.parseStatements(sql);
        List<RiskScript> riskScripts = new ArrayList<>();
        for (Statement statement : statements.getStatements()) {
            RiskScript riskScript = new RiskScript();

            //drop table
            if (statement instanceof Drop
                    && (((Drop) statement).getType().equals("table")
                    || ((Drop) statement).getType().equals("TABLE"))) {
                riskScript.setErrorMsg("drop table高危，放弃自动执行，请确认，如有需要请手动执行");
                riskScript.setSql(statement.toString());
                riskScripts.add(riskScript);
                continue;
            }

            //update不带where条件
            if (statement instanceof Update
                    && ((Update) statement).getWhere() == null) {
                riskScript.setErrorMsg("update 不带where条件，放弃自动执行，请确认，如有需要请手动执行");
                riskScript.setSql(statement.toString());
                riskScripts.add(riskScript);
                continue;
            }

            //delete不带where条件
            if (statement instanceof Delete &&
                    ((Delete) statement).getWhere() == null) {
                riskScript.setErrorMsg("delete 不带where条件，放弃自动执行，请确认，如有需要请手动执行");
                riskScript.setSql(statement.toString());
                riskScripts.add(riskScript);
                continue;
            }

        }

        log.info("{}", JsonUtil.toJson(riskScripts));
    }

    public static void validDDL() {
        Pattern pattern = Pattern.compile("line (\\d+), column (\\d+)");

        try {
            String sql = "SELECT * FROM TABLE1;SELECT * FORM TABLE2";
            CCJSqlParserUtil.parseStatements(sql);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            String message = e.getMessage();
            Matcher m = pattern.matcher(message);
            int line = -1;
            int column = -1;
            while (m.find()) {
                int groupCount = m.groupCount();
                if (groupCount > 0) {
                    line = Integer.parseInt(m.group(1));
                    column = Integer.parseInt(m.group(2));
                    break;
                }
            }

            log.info("line = {}, column = {}", line, column);
        }
    }

    /**
     * 获取SQL中的字段名称
     * @param sql
     */
    public static void columnBySQL(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);

        if (statement instanceof Select) {
            Select selectStatement = (Select) statement;

            PlainSelect ps = (PlainSelect)selectStatement.getSelectBody();

            List<SelectItem> selectitems = ps.getSelectItems();
            selectitems.forEach(System.out::println);
        } else if (statement instanceof CreateTable) {
            CreateTable createTable = (CreateTable) statement;

            createTable.getColumnDefinitions().forEach(v -> System.out.println(v.getColumnName()));
        }
    }

    /**
     * 获取SQL中的表名称
     *
     * @throws Exception
     */
    public static void tablesNameBySQL() throws Exception {
        String sql = "select * from outvisit l " +
                "left join patient p on l.patid=p.patientid " +
                "left join patstatic c on l.patid=c.patid " +
                "left join patphone ph on l.patid=ph.patid " +
                "where l.name='kevin' " +
                "union all " +
                "select * from invisit v";

        System.out.println(sql);

        Statement statement = CCJSqlParserUtil.parse(sql);

        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> result = tablesNamesFinder.getTableList(statement);
        for (String tableStr : result) {
            log.info(">>>> " + tableStr);
        }
    }

    public static void main(String[] args) throws Exception {
        //highRisk("DELETE from aaa");
        // checkAllItem();
        // validate();
        // columnBySQL("select a from (select b from table1 left join table2 on table2.c=table1.d)");
        columnBySQL("create table ttt(id int(10),name varchar(20))");
    }

}
