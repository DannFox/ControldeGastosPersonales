using APIGastosPersonales.Data;
using APIGastosPersonales.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace APIGastosPersonales.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GastosController : ControllerBase
    {
        private readonly AppDbContext _context;

        public GastosController(AppDbContext context)
        {
            _context = context;
        }

        // GET api/gastos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Gasto>>> GetGastos()
        {
            return await _context.Gastos.Include(g => g.Cuenta).ToListAsync();
        }

        // GET api/gastos/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Gasto>> GetGasto(int id)
        {
            var gasto = await _context.Gastos.Include(g => g.Cuenta)
                .FirstOrDefaultAsync(g => g.Id_gasto == id);
            if (gasto == null) return NotFound();
            return gasto;
        }

        // POST api/gastos
        [HttpPost]
        public async Task<ActionResult<Gasto>> PostGasto(Gasto gasto)
        {
            _context.Gastos.Add(gasto);
            await _context.SaveChangesAsync();
            return CreatedAtAction(nameof(GetGasto), new { id = gasto.Id_gasto }, gasto);
        }

        // PUT api/gastos/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutGasto(int id, Gasto gasto)
        {
            if (id != gasto.Id_gasto) return BadRequest();

            _context.Entry(gasto).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return NoContent();
        }

        // DELETE api/gastos/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteGasto(int id)
        {
            var gasto = await _context.Gastos.FindAsync(id);
            if (gasto == null) return NotFound();

            _context.Gastos.Remove(gasto);
            await _context.SaveChangesAsync();
            return NoContent();
        }

        // GET api/gastos/balance
        [HttpGet("balance")]
        public async Task<ActionResult<decimal>> GetBalance()
        {
            return await _context.Gastos.SumAsync(g => g.Monto);
        }

        // GET api/gastos/porcuenta/5
        [HttpGet("porcuenta/{cuentaId}")]
        public async Task<ActionResult<IEnumerable<Gasto>>> GetGastosPorCuenta(int cuentaId)
        {
            var gastos = await _context.Gastos
                .Where(g => g.CuentaId == cuentaId)
                .Include(g => g.Cuenta)
                .ToListAsync();
            return gastos;
        }
    }
}
