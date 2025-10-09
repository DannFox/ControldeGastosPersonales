using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace APIGastosPersonales.Models
{
    public class Ingreso
    {
        [Key]
        public int Id_ingreso { get; set; }
        [Required(ErrorMessage = "El monto es obligatorio")]
        [Column(TypeName = "decimal(18,2)")]
        [Range(0.01, double.MaxValue, ErrorMessage = "El monto debe ser mayor que cero")]
        public decimal Monto { get; set; }
        [MaxLength(250)]
        public string Descripcion { get; set; } = string.Empty;
        [Required]
        public DateTime Fecha { get; set; } = DateTime.Now;

      //Relaciones
        public int CuentaId { get; set; }
        public Cuenta? Cuenta { get; set; }

        public int CategoriaId { get; set; }
        public Categoria? Categoria { get; set; }
    }
}
