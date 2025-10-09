using APIGastosPersonales.Data;
using APIGastosPersonales.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace APIGastosPersonales.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class IngresosController : ControllerBase
    {
        private readonly AppDbContext _context;

        public IngresosController(AppDbContext context)
        {
            _context = context;
        }

        // GET api/ingresos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Ingreso>>> GetIngresos()
        {
            return await _context.Ingresos.Include(i => i.Cuenta).ToListAsync();
        }

        // GET api/ingresos/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Ingreso>> GetIngreso(int id)
        {
            var ingreso = await _context.Ingresos.Include(i => i.Cuenta)
                .FirstOrDefaultAsync(i => i.Id_ingreso == id);
            if (ingreso == null) return NotFound();
            return ingreso;
        }

        // POST api/ingresos
        [HttpPost]
        public async Task<ActionResult<Ingreso>> PostIngreso(Ingreso ingreso)
        {
            _context.Ingresos.Add(ingreso);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetIngreso), new { id = ingreso.Id_ingreso }, ingreso);
        }

        // PUT api/ingresos/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutIngreso(int id, Ingreso ingreso)
        {
            if (id != ingreso.Id_ingreso) return BadRequest();

            _context.Entry(ingreso).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return NoContent();
        }

        // DELETE api/ingresos/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteIngreso(int id)
        {
            var ingreso = await _context.Ingresos.FindAsync(id);
            if (ingreso == null) return NotFound();

            _context.Ingresos.Remove(ingreso);
            await _context.SaveChangesAsync();
            return NoContent();
        }

        // GET api/ingresos/porcuenta/3
        [HttpGet("porcuenta/{cuentaId}")]
        public async Task<ActionResult<IEnumerable<Ingreso>>> GetIngresosPorCuenta(int cuentaId)
        {
            var ingresos = await _context.Ingresos
                .Where(i => i.CuentaId == cuentaId)
                .Include(i => i.Cuenta)
                .ToListAsync();
            return ingresos;
        }

        // GET api/ingresos/total
        [HttpGet("total")]
        public async Task<ActionResult<decimal>> GetTotalIngresos()
        {
            return await _context.Ingresos.SumAsync(i => i.Monto);
        }
    }
}
