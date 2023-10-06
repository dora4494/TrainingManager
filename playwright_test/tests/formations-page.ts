import {test, expect, type Page, Locator} from '@playwright/test';

export class FormationsPage {
    readonly page: Page;
    readonly boutonEnregister: Locator
    readonly titre: Locator
    readonly items_formation: Locator

    constructor(page: Page) {
        this.page = page
        this.boutonEnregister = page.locator('button', { name: 'Enregistrer' })
        this.titre = page.locator('h2')
        this.items_formation = page.locator('a')
    }

    async ouvrir() {
       const host = process.env.APP_HOST as string ?? 'localhost:8080' ;
       await this.page.goto('http://' + host + '/formations');
    }

    async ajouter_formation(titre: String) {
        await this.page.getByRole('button', { name: 'Ajouter une formation' }).click();
        this.page.getByPlaceholder('Intitule').fill(titre);
       // await this.boutonEnregister.click();
        await this.page.getByRole('button', { name: 'Enregistrer' }).click();
        await expect(this.titre).toHaveText([
            "Liste des formations :"
        ]);
    }
}