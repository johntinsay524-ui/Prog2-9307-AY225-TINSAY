function runMP19(rows) {
  const typeCounts = {}, examCounts = {}, examGroups = {};
  let pass = 0, fail = 0;
  const scores = [];

  rows.forEach(r => {
    typeCounts[r[1]] = (typeCounts[r[1]] || 0) + 1;
    examCounts[r[3]] = (examCounts[r[3]] || 0) + 1;

    if (!examGroups[r[3]]) examGroups[r[3]] = { pass: 0, total: 0 };
    examGroups[r[3]].total++;
    if (r[7] === 'PASS') { pass++; examGroups[r[3]].pass++; } else fail++;

    const s = parseInt(r[6]);
    if (!isNaN(s)) scores.push(s);
  });

  const passRate  = (pass / rows.length * 100).toFixed(1);
  const min       = Math.min(...scores);
  const max       = Math.max(...scores);
  const avg       = (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1);

  const typeColors = { Student: '#7c3aed', Faculty: '#00e5ff', NTE: '#f59e0b' };
  const topExams   = Object.entries(examCounts).sort((a, b) => b[1] - a[1]).slice(0, 5);
  const maxEC      = topExams[0]?.[1] || 1;
  const passRates  = Object.entries(examGroups)
    .map(([exam, { pass: p, total }]) => ({ exam, rate: p / total * 100 }))
    .sort((a, b) => b.rate - a.rate);

  const typeHtml = Object.entries(typeCounts).map(([t, c]) => `
    <span class="type-pill" style="background:${typeColors[t]||'#444'}22;border:1px solid ${typeColors[t]||'#444'}44;color:${typeColors[t]||'#ccc'}">
      ${t} <span class="count">${c}</span>
    </span>`).join('');

  const topExamHtml = topExams.map(([exam, count], i) => `
    <div class="exam-row" style="animation-delay:${i * 0.07}s">
      <span class="exam-rank">${i + 1}</span>
      <span class="exam-name">${exam}</span>
      <div class="exam-bar-wrap"><div class="exam-bar" style="width:0%" data-w="${count / maxEC * 100}%"></div></div>
      <span class="exam-count">${count}</span>
    </div>`).join('');

  const passRateHtml = passRates.map(({ exam, rate }) => {
    const color = rate >= 80 ? 'var(--pass)' : rate >= 50 ? 'var(--accent3)' : 'var(--fail)';
    return `
    <div class="pass-rate-row">
      <span class="pr-name">${exam}</span>
      <div class="pr-bar-wrap"><div class="pr-bar" style="width:0%;background:${color}" data-w="${rate}%"></div></div>
      <span class="pr-pct" style="color:${color}">${rate.toFixed(1)}%</span>
    </div>`;
  }).join('');

  document.getElementById('mp19-results').innerHTML = `
    <div class="summary-grid">
      <div class="summary-card">
        <div class="card-title">Overview</div>
        <div class="big-stat">${rows.length}</div>
        <div class="big-stat-label">Total Records</div>
        <div style="margin-top:16px">${typeHtml}</div>
      </div>
      <div class="summary-card">
        <div class="card-title">Score Statistics</div>
        <div class="score-row"><span class="key">Min</span><span class="val">${min}</span></div>
        <div class="score-row"><span class="key">Max</span><span class="val">${max}</span></div>
        <div class="score-row"><span class="key">Average</span><span class="val">${avg}</span></div>
        <div class="score-row"><span class="key">Unique Exams</span><span class="val">${Object.keys(examCounts).length}</span></div>
      </div>
      <div class="summary-card">
        <div class="card-title">Pass / Fail Results</div>
        <div style="display:flex;gap:24px;margin-bottom:12px">
          <div><div style="font-size:28px;font-weight:800;color:var(--pass)">${pass}</div><div style="font-family:var(--font-mono);font-size:11px;color:var(--muted)">PASS</div></div>
          <div><div style="font-size:28px;font-weight:800;color:var(--fail)">${fail}</div><div style="font-family:var(--font-mono);font-size:11px;color:var(--muted)">FAIL</div></div>
          <div><div style="font-size:28px;font-weight:800;color:var(--accent3)">${passRate}%</div><div style="font-family:var(--font-mono);font-size:11px;color:var(--muted)">PASS RATE</div></div>
        </div>
        <div class="pass-fail-bar"><div class="pass-fill" style="width:0%" data-w="${passRate}%"></div></div>
        <div class="pf-labels"><span class="p">Pass ${passRate}%</span><span class="f">Fail ${(100 - parseFloat(passRate)).toFixed(1)}%</span></div>
      </div>
      <div class="summary-card">
        <div class="card-title">Top 5 Exams by Enrollment</div>
        ${topExamHtml}
      </div>
      <div class="summary-card full">
        <div class="card-title">Exam Pass Rates</div>
        ${passRateHtml}
      </div>
    </div>
  `;
}
