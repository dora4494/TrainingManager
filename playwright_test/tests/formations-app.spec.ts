import { test, expect, type Page } from '@playwright/test';

test.use({
  ignoreHTTPSErrors: true,
}); 

test.beforeEach(async ({ page }) => {
  //await page.goto('http://localhost:6868/formations');
  await page.goto('http://app:8080/formations');
});



test.describe('New Todo', () => {
  test('should display first page', async ({ page }) => {
    

    // Make sure the list only has one todo item.
    await expect(page.getByRole('heading')).toHaveText([
      "Liste des formations :"
    ]);

    const nb_items = await page.getByRole('link').count();
    await page.getByRole('button', { name: 'Ajouter une formation' }).click();
    page.getByPlaceholder('Intitule').fill("DDD");
    await page.getByRole('button', { name: 'Enregistrer' }).click();

    await expect(page.getByRole('link')).toHaveCount(nb_items + 1);
  });
});
