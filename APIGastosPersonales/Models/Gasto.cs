using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace APIGastosPersonales.Models
{
    public class Gasto
    {
        [Key]
        public int Id_gasto { get; set; }
        [Required(ErrorMessage = "El campo Categoria es obligatorio")]
        [MaxLength(50)]
        public string Categoria { get; set; } = string.Empty;
        [Required(ErrorMessage = "El monto es obligatorio")]
        [Column(TypeName = "decimal(18,2)")]
        [Range(0.01, double.MaxValue, ErrorMessage = "El monto debe ser mayor que cero")]
        public decimal Monto { get; set; }
        [MaxLength(250)]
        public string Descripcion { get; set; } = string.Empty;
        [Required]
        public DateTime Fecha { get; set; } = DateTime.Now;

        [Required]
        public int CuentaId { get; set; }
        public Cuenta? Cuenta { get; set; }
    }
}
