using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace APIGastosPersonales.Migrations
{
    /// <inheritdoc />
    public partial class Pepito : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Categoria",
                table: "Gastos");

            migrationBuilder.AddColumn<int>(
                name: "CategoriaId",
                table: "Gastos",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateTable(
                name: "Categorias",
                columns: table => new
                {
                    Id_categoria = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Nombre = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    Tipo = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Categorias", x => x.Id_categoria);
                });

            migrationBuilder.CreateTable(
                name: "Ingresos",
                columns: table => new
                {
                    Id_ingreso = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Monto = table.Column<decimal>(type: "decimal(18,2)", nullable: false),
                    Descripcion = table.Column<string>(type: "nvarchar(250)", maxLength: 250, nullable: false),
                    Fecha = table.Column<DateTime>(type: "datetime2", nullable: false),
                    CuentaId = table.Column<int>(type: "int", nullable: false),
                    CategoriaId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Ingresos", x => x.Id_ingreso);
                    table.ForeignKey(
                        name: "FK_Ingresos_Categorias_CategoriaId",
                        column: x => x.CategoriaId,
                        principalTable: "Categorias",
                        principalColumn: "Id_categoria",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Ingresos_Cuentas_CuentaId",
                        column: x => x.CuentaId,
                        principalTable: "Cuentas",
                        principalColumn: "Id_cuenta",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.InsertData(
                table: "Categorias",
                columns: new[] { "Id_categoria", "Nombre", "Tipo" },
                values: new object[,]
                {
                    { 1, "Alimentacion", "Gasto" },
                    { 2, "Transporte", "Gasto" },
                    { 3, "Entretenimiento", "Gasto" },
                    { 4, "Educación", "Gasto" },
                    { 5, "Salud", "Gasto" },
                    { 6, "Otros", "Gasto" },
                    { 7, "Salario", "Ingreso" },
                    { 8, "Venta", "Ingreso" },
                    { 9, "Intereses", "Ingreso" },
                    { 10, "Regalo", "Ingreso" },
                    { 11, "Reembolso", "Ingreso" },
                    { 12, "Otros", "Ingreso" }
                });

            migrationBuilder.CreateIndex(
                name: "IX_Gastos_CategoriaId",
                table: "Gastos",
                column: "CategoriaId");

            migrationBuilder.CreateIndex(
                name: "IX_Ingresos_CategoriaId",
                table: "Ingresos",
                column: "CategoriaId");

            migrationBuilder.CreateIndex(
                name: "IX_Ingresos_CuentaId",
                table: "Ingresos",
                column: "CuentaId");

            migrationBuilder.AddForeignKey(
                name: "FK_Gastos_Categorias_CategoriaId",
                table: "Gastos",
                column: "CategoriaId",
                principalTable: "Categorias",
                principalColumn: "Id_categoria",
                onDelete: ReferentialAction.Restrict);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Gastos_Categorias_CategoriaId",
                table: "Gastos");

            migrationBuilder.DropTable(
                name: "Ingresos");

            migrationBuilder.DropTable(
                name: "Categorias");

            migrationBuilder.DropIndex(
                name: "IX_Gastos_CategoriaId",
                table: "Gastos");

            migrationBuilder.DropColumn(
                name: "CategoriaId",
                table: "Gastos");

            migrationBuilder.AddColumn<string>(
                name: "Categoria",
                table: "Gastos",
                type: "nvarchar(50)",
                maxLength: 50,
                nullable: false,
                defaultValue: "");
        }
    }
}
