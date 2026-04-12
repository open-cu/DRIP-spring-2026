package ru.cu.springshelldemo.command;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

@Command
public class Example10ModernTableOutputCommand {

	@Command(command = "m-show-users", description = "Show users table")
	public Table showUsers() {
		TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

		modelBuilder.addRow()
				.addValue("ID")
				.addValue("Name")
				.addValue("Email")
				.addValue("Age");

		modelBuilder.addRow()
				.addValue("1")
				.addValue("Ivan Petrov")
				.addValue("ivan@example.com")
				.addValue("25");

		modelBuilder.addRow()
				.addValue("2")
				.addValue("Maria Sidorova")
				.addValue("maria@example.com")
				.addValue("30");

		modelBuilder.addRow()
				.addValue("3")
				.addValue("Alexey Smirnov")
				.addValue("alexey@example.com")
				.addValue("28");

		TableModel model = modelBuilder.build();
		return new TableBuilder(model)
				.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.center).and()
				.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(3)).addAligner(SimpleHorizontalAligner.center).and()
				.addFullBorder(BorderStyle.fancy_light)
				.build();
	}

	@Command(command = "m-show-products", description = "Show products table")
	public Table showProducts() {
		TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

		modelBuilder.addRow().addValue("Product").addValue("Price").addValue("Stock");
		modelBuilder.addRow().addValue("Laptop").addValue("75000 ₽").addValue("15");
		modelBuilder.addRow().addValue("Mouse").addValue("2500 ₽").addValue("100");
		modelBuilder.addRow().addValue("Keyboard").addValue("4500 ₽").addValue("45");

		TableModel model = modelBuilder.build();
		return new TableBuilder(model)
				.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.right).and()
				.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.center).and()
				.addFullBorder(BorderStyle.fancy_light)
				.build();
	}
}
