using APIGastosPersonales.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.Identity.Client;

namespace APIGastosPersonales.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {}

        public DbSet<Cuenta> Cuentas { get; set; }
        public DbSet<Gasto> Gastos { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Cuenta>(entity =>
            {
                //Clave primaria
                entity.HasKey(e => e.Id_cuenta);

                //Propiedades
                entity.Property(e => e.Nombre).IsRequired().HasMaxLength(100);
                entity.Property(e => e.SaldoInicial).HasPrecision(18, 2);
                entity.Property(e => e.SaldoActual).HasPrecision(18, 2);
            });

            modelBuilder.Entity<Gasto>(entity =>
            {
                //Clave primaria
                entity.HasKey(e => e.Id_gasto);
                //Propiedades
                entity.Property(e => e.Categoria).IsRequired().HasMaxLength(50);
                entity.Property(e => e.Monto).HasPrecision(18, 2);
                entity.Property(e => e.Descripcion).HasMaxLength(250);
                entity.Property(e => e.Fecha).HasDefaultValueSql("CURRENT_TIMESTAMP"); //fecha por defecto
                //Relación con Cuenta
                entity.HasOne(e => e.Cuenta)
                      .WithMany(c => c.Gastos)
                      .HasForeignKey(e => e.CuentaId)
                      .OnDelete(DeleteBehavior.Cascade);
            });

        }
    }
}
