const REQUIRED = [0, 1, 3, 4, 5, 6, 7, 8];

function runMP18(rows) {
  const clean   = rows.filter(r => REQUIRED.every(idx => r[idx].trim() !== ''));
  const removed = rows.filter(r => REQUIRED.some(idx  => r[idx].trim() === ''));

  let html = `
    <div class="stat-row">
      <div class="stat-box">
        <div class="num">${rows.length}</div>
        <div class="lbl">Total Rows</div>
      </div>
      <div class="stat-box removed">
        <div class="num">${removed.length}</div>
        <div class="lbl">Rows Removed</div>
      </div>
      <div class="stat-box clean">
        <div class="num">${clean.length}</div>
        <div class="lbl">Clean Rows</div>
      </div>
    </div>
    <div class="section-label">Result</div>
    <div class="removed-list">
  `;

  if (removed.length === 0) {
    html += `<div class="all-clean">✅ All ${rows.length} rows are complete — nothing removed.</div>`;
  } else {
    html += `<div class="removed-list-header">Removed Rows (${removed.length})</div>`;
    removed.forEach(r => {
      html += `<div class="removed-item">${r[0] || '(unnamed)'}</div>`;
    });
  }

  html += '</div>';
  document.getElementById('mp18-results').innerHTML = html;
}
