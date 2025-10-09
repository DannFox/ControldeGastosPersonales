using System.ComponentModel.DataAnnotations;

namespace APIGastosPersonales.DTOs
{
    public class CrearCategoriaDTO
    {
        [Required(ErrorMessage = "El nombre de la categoría es obligatorio.")]
        [MaxLength(50, ErrorMessage = "El nombre no puede superar 50 caracteres.")]
        public string Nombre { get; set; } = string.Empty;

        [Required(ErrorMessage = "Debe especificar el tipo de categoría.")]
        [RegularExpression("Gasto|Ingreso", ErrorMessage = "El tipo debe ser 'Gasto' o 'Ingreso'.")]
        public string Tipo { get; set; } = string.Empty; // "Gasto" o "Ingreso"
    }
}
