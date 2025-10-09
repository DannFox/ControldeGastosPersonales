using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace APIGastosPersonales.Migrations
{
    /// <inheritdoc />
    public partial class Pepito2 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "EsPredefinida",
                table: "Categorias",
                type: "bit",
                nullable: false,
                defaultValue: false);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 1,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 2,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 3,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 4,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 5,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 6,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 7,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 8,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 9,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 10,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 11,
                column: "EsPredefinida",
                value: true);

            migrationBuilder.UpdateData(
                table: "Categorias",
                keyColumn: "Id_categoria",
                keyValue: 12,
                column: "EsPredefinida",
                value: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "EsPredefinida",
                table: "Categorias");
        }
    }
}
