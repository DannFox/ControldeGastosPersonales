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
        public DbSet<Ingreso> Ingresos { get; set; }
        public DbSet<Categoria> Categorias { get; set; }
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
                entity.Property(e => e.Monto).HasPrecision(18, 2);
                entity.Property(e => e.Descripcion).HasMaxLength(250);
                entity.Property(e => e.Fecha).HasDefaultValueSql("CURRENT_TIMESTAMP"); //fecha por defecto
                //Relación con Cuenta
                entity.HasOne(e => e.Cuenta)
                      .WithMany(c => c.Gastos)
                      .HasForeignKey(e => e.CuentaId)
                      .OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<Categoria>(entity =>
            {
                entity.Property(c => c.Nombre)
                .IsRequired()
                .HasMaxLength(50);

                entity.Property(c => c.Tipo)
                .IsRequired()
                .HasMaxLength(10);

                entity.HasData(
                    //Categorias de gastos
                    new Categoria { Id_categoria = 1, Nombre = "Alimentacion", Tipo = "Gasto", EsPredefinida = true },
                    new Categoria { Id_categoria = 2, Nombre = "Transporte", Tipo = "Gasto", EsPredefinida = true },
                    new Categoria { Id_categoria = 3, Nombre = "Entretenimiento", Tipo = "Gasto", EsPredefinida = true },
                    new Categoria { Id_categoria = 4, Nombre = "Educación", Tipo = "Gasto", EsPredefinida = true },
                    new Categoria { Id_categoria = 5, Nombre = "Salud", Tipo = "Gasto", EsPredefinida = true },
                    new Categoria { Id_categoria = 6, Nombre = "Otros", Tipo = "Gasto", EsPredefinida = true },

                    //Categorias de Ingreso
                    new Categoria { Id_categoria = 7, Nombre = "Salario", Tipo = "Ingreso", EsPredefinida = true },
                    new Categoria { Id_categoria = 8, Nombre = "Venta", Tipo = "Ingreso", EsPredefinida = true },
                    new Categoria { Id_categoria = 9, Nombre = "Intereses", Tipo = "Ingreso", EsPredefinida = true },
                    new Categoria { Id_categoria = 10, Nombre = "Regalo", Tipo = "Ingreso", EsPredefinida = true },
                    new Categoria { Id_categoria = 12, Nombre = "Otros", Tipo = "Ingreso", EsPredefinida = true }
                    );
            });

            modelBuilder.Entity<Gasto>()
                .HasOne(g => g.Categoria)
                .WithMany(c => c.Gastos)
                .HasForeignKey(g => g.CategoriaId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Ingreso>()
                .HasOne(i => i.Categoria)
                .WithMany(c => c.Ingresos)
                .HasForeignKey(i => i.CategoriaId)
                .OnDelete(DeleteBehavior.Restrict);

        }
    }
}
