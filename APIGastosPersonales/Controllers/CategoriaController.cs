using APIGastosPersonales.Data;
using APIGastosPersonales.DTOs;
using APIGastosPersonales.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace APIGastosPersonales.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CategoriaController : ControllerBase
    {
        private readonly AppDbContext _context;

        public CategoriaController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Categoria>>> GetCategorias()
        {
            return await _context.Categorias.ToListAsync();
        }

        [HttpGet("{tipo}")]
        public async Task<ActionResult<IEnumerable<Categoria>>> GetPorTipo(string tipo)
        {
            var categorias = await _context.Categorias
                .Where(c => c.Tipo == tipo)
                .ToListAsync();

            return categorias;
        }

        // POST: api/categorias
        [HttpPost]
        public async Task<ActionResult<Categoria>> CrearCategoria(CrearCategoriaDTO dto)
        {
            // Validar si ya existe una categoría con ese nombre y tipo
            var existe = await _context.Categorias
                .AnyAsync(c => c.Nombre.ToLower() == dto.Nombre.ToLower() && c.Tipo == dto.Tipo);

            if (existe)
                return Conflict("Ya existe una categoría con ese nombre y tipo.");

            var categoria = new Categoria
            {
                Nombre = dto.Nombre,
                Tipo = dto.Tipo
            };

            _context.Categorias.Add(categoria);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetPorTipo),
                new { tipo = categoria.Tipo }, categoria);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> EditarCategoria(int id, CrearCategoriaDTO dto)
        {
            var categoria = await _context.Categorias.FindAsync(id);
            if (categoria == null)
                return NotFound("Categoría no encontrada.");

            if (categoria.EsPredefinida)
                return BadRequest("No se puede editar una categoría predefinida.");

            // Validar duplicados
            var existe = await _context.Categorias
                .AnyAsync(c => c.Id_categoria != id && c.Nombre.ToLower() == dto.Nombre.ToLower() && c.Tipo == dto.Tipo);

            if (existe)
                return Conflict("Ya existe una categoría con ese nombre y tipo.");

            categoria.Nombre = dto.Nombre;
            categoria.Tipo = dto.Tipo;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> EliminarCategoria(int id)
        {
            var categoria = await _context.Categorias
                .Include(c => c.Gastos)
                .Include(c => c.Ingresos)
                .FirstOrDefaultAsync(c => c.Id_categoria == id);

            if (categoria == null)
                return NotFound("Categoría no encontrada.");

            if (categoria.EsPredefinida)
                return BadRequest("No se puede eliminar una categoría predefinida.");

            // Opcional: verificar si tiene movimientos asociados
            if (categoria.Gastos.Any() || categoria.Ingresos.Any())
                return BadRequest("No se puede eliminar una categoría que tenga movimientos asociados.");

            _context.Categorias.Remove(categoria);
            await _context.SaveChangesAsync();

            return NoContent();
        }

    }
}
