using APIGastosPersonales.Data;
using APIGastosPersonales.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace APIGastosPersonales.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CuentasController : ControllerBase
    {
        private readonly AppDbContext _context;
        public CuentasController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Cuenta>>> GetCuentas()
        {
            return await _context.Cuentas.Include(c => c.Gastos).ToListAsync();
        }

        // GET api/cuentas/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Cuenta>> GetCuenta(int id)
        {
            var cuenta = await _context.Cuentas
                .Include(c => c.Gastos)
                .FirstOrDefaultAsync(c => c.Id_cuenta == id);

            if (cuenta == null) return NotFound();
            return cuenta;
        }

        // POST api/cuentas
        [HttpPost]
        public async Task<ActionResult<Cuenta>> PostCuenta(Cuenta cuenta)
        {
            cuenta.SaldoActual = cuenta.SaldoInicial;
            _context.Cuentas.Add(cuenta);
            await _context.SaveChangesAsync();
            return CreatedAtAction(nameof(GetCuenta), new { id = cuenta.Id_cuenta }, cuenta);
        }

        // PUT api/cuentas/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCuenta(int id, Cuenta cuenta)
        {
            if (id != cuenta.Id_cuenta) return BadRequest();

            _context.Entry(cuenta).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return NoContent();
        }

        // DELETE api/cuentas/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCuenta(int id)
        {
            var cuenta = await _context.Cuentas.FindAsync(id);
            if (cuenta == null) return NotFound();

            _context.Cuentas.Remove(cuenta);
            await _context.SaveChangesAsync();
            return NoContent();
        }

        // GET api/cuentas/5/saldo
        [HttpGet("{id}/saldo")]
        public async Task<ActionResult<decimal>> GetSaldo(int id)
        {
            var cuenta = await _context.Cuentas
                .Include(c => c.Gastos)
                .FirstOrDefaultAsync(c => c.Id_cuenta == id);

            if (cuenta == null) return NotFound();

            decimal saldo = cuenta.SaldoInicial - cuenta.Gastos.Sum(g => g.Monto);
            return saldo;
        }
    }
}
