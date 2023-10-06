import { test, expect, type Page } from '@playwright/test';
import { FormationsPage } from './formations-page';

test.use({
  ignoreHTTPSErrors: true,
}); 

let page_formations;
test.beforeEach(async ({ page }) => {
    page_formations = new FormationsPage(page);
    page_formations.ouvrir();
});

test.describe('Formations', () => {
  test('Ajouter une formation inexistante', async ({ page }) => {

    await expect(page_formations.titre).toHaveText([
      "Liste des formations :"
    ]);

    const nb_items = await page_formations.items_formation.count();

    let myuuid = new Date().getTime();
    let nom_formation = 'Test_' + myuuid

    await expect(page_formations.items_formation.filter({ hasText: nom_formation})).toHaveCount(0);

    page_formations.ajouter_formation(nom_formation);

    await expect(page_formations.items_formation).toHaveCount(nb_items + 1);
    await expect(page_formations.items_formation.filter({ hasText: nom_formation})).toHaveCount(1);

  });

  test('Ajouter une formation existante', async ({ page }) => {

    await expect(page_formations.titre).toHaveText([
      "Liste des formations :"
    ]);

    const nb_items = await page_formations.items_formation.count();

    let myuuid = new Date().getTime();
    let nom_formation = 'Test_' + myuuid

    await expect(page_formations.items_formation.filter({ hasText: nom_formation})).toHaveCount(0);

    page_formations.ajouter_formation(nom_formation);

    await expect(page_formations.items_formation).toHaveCount(nb_items + 1);
    await expect(page_formations.items_formation.filter({ hasText: nom_formation})).toHaveCount(1);

    page_formations.ajouter_formation(nom_formation);

    await expect(page_formations.items_formation).toHaveCount(nb_items + 1);
    await expect(page_formations.items_formation.filter({ hasText: nom_formation})).toHaveCount(1);

  });
});
