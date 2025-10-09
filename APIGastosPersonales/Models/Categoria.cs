using System.ComponentModel.DataAnnotations;

namespace APIGastosPersonales.Models
{
    public class Categoria
    {
        [Key]
        public int Id_categoria { get; set; }

        [Required(ErrorMessage = "El nombre de la categoría es obligatorio.")]
        [MaxLength(50)]
        public string Nombre { get; set; } = string.Empty;

        [Required(ErrorMessage = "Debe indicar el tipo de categoría.")]
        [MaxLength(10)]
        public string Tipo { get; set; } = string.Empty; // "Gasto" o "Ingreso"

        // Relaciones
        public ICollection<Gasto> Gastos { get; set; } = new List<Gasto>();
        public ICollection<Ingreso> Ingresos { get; set; } = new List<Ingreso>();
    }
}
