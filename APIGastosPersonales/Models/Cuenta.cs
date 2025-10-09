using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace APIGastosPersonales.Models
{
    public class Cuenta
    {
        [Key]
        public int Id_cuenta { get; set; }
        [Required(ErrorMessage = "El campo Nombre es obligatorio")]
        [MaxLength(100, ErrorMessage = "El campo Nombre no puede exceder los 100 caracteres")]
        public string Nombre { get; set; }
        [Column(TypeName = "decimal(18,2)")]
        [Range(0, double.MaxValue, ErrorMessage = "El campo SaldoInicial no puede ser negativo")]
        public decimal SaldoInicial { get; set; } = 0;
        [Column(TypeName = "decimal(18,2)")]
        public decimal SaldoActual { get; set; } = 0;

        public ICollection<Gasto> Gastos { get; set; } = new List<Gasto>();
    }
}
