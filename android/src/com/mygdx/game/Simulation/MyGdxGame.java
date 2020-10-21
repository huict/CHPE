package com.mygdx.game.Simulation;

import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Analysis.Data;
import com.mygdx.game.Analysis.DatabaseData;
import com.mygdx.game.HomeScreen;
import com.mygdx.game.Persistance.PersistenceClient;

/**
 * This is the main application class.
 * This is were all the magic happens and the applications gets its final form.
 */
public class MyGdxGame implements ApplicationListener {
	public PerspectiveCamera gameCam;
	public PerspectiveCamera guiCam;
	public CameraInputController camController;
	public Shader shader;
	public ModelBuilder modelBuilder;
	public AssetManager assets;
	public ModelBatch modelBatch;
	public Batch batch;
	public Environment environment;
	public BitmapFont font;
	public Body body;
	public Context context;
	public Data data;
	public CubeAnalyse cube_analyse;

	//todo Make functions to change the clear color and the player model colors from this main class.
	public Color backgroundColor;
	public Color preppieColor;

	public int fieldOfView = 67;
	private int frame = 0;
	private int tick = 0;
	public boolean loading;

	/**
	 * The create function creates the application.
	 * The cameras are created and identified here.
	 * All the needed objects for the application are created and initialized here.
	 */
	@Override
	public void create(){

		//Data object
		context = HomeScreen.getAppContext();
		data = new DatabaseData(PersistenceClient.getInstance(context).getAppDatabase());

		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		body = new Body();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		gameCam = new PerspectiveCamera(fieldOfView, 1, 1);
		gameCam.position.set(1f, 1f, 20f);
		gameCam.lookAt(0,0,0);
		gameCam.near = 1f;
		gameCam.far = 300f;
		gameCam.update();

		guiCam = new PerspectiveCamera(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		guiCam.position.set(1f, 1f, 20f);
		guiCam.lookAt(0, 0, 0);
		guiCam.near = 1f;
		guiCam.far = 300f;
		guiCam.update();

		camController = new CameraInputController(gameCam);
		Gdx.input.setInputProcessor(camController);

		body.create(1f, data);

		assets = new AssetManager();
		assets.load("data/human.obj", Model.class);
		loading = true;
		shader = new TestShader();
		shader.init();

		modelBatch = new ModelBatch();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(guiCam.combined);

		font = new BitmapFont();
		font.setColor(Color.RED);
		// ============================================================================
		cube_analyse = new CubeAnalyse();
		// ============================================================================
	}

	/**
	 * This function is no longer needed for the current project, but might be needed later when we want to use actual 3D models (obj i.e.).
	 * What this function does is it pre-loads the asset beforehand. You do not want this to be done during rendering.
	 */
	private void doneLoading(){
		Model human = assets.get("data/human.obj", Model.class);
		ModelInstance humanInstance = new ModelInstance(human);
		ColorAttribute attributeU = new TestShader.TestColorAttribute(TestShader.TestColorAttribute.DiffuseU, 1f, 0f, 0f, 1);
		humanInstance.materials.get(0).set(attributeU);
		ColorAttribute attributeV = new TestShader.TestColorAttribute(TestShader.TestColorAttribute.DiffuseV, 1f, 0f, 0f, 1);
		humanInstance.materials.get(0).set(attributeV);
		humanInstance.transform.setToRotation(Vector3.Z,90);
		humanInstance.transform.setTranslation(10,0, 0);
		loading = false;
	}

	/**
	 * This is the main loop, going at a freq. of 60Hz.
	 */
	@Override
	public void render(){
		if (loading && assets.update()) {
			doneLoading();
		}
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0f, 0.631f, 0.882f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// |-------------- test environment ------------|
		tick ++;
		if(tick >= 60 / 24){
			body.update(frame, data);
			frame++;
			tick = 0;
		}
		if(frame >= data.getFrameCount()-1){
			frame = 0;
		}
		// |--------------------------------------------|
		modelBatch.begin(gameCam);
		modelBatch.render(body.getJointArray(), environment);
		modelBatch.render(body.getLimbArray(), environment);
		cube_analyse.update(data, frame);
		modelBatch.render(cube_analyse.renderables(), environment);
		modelBatch.end();

		batch.begin();

		// Render GUI items
//		HelperClass.DrawDebugLine(new Vector2(0,-200), new Vector2(0,200), 4, Color.RED, guiCam.combined);
//		HelperClass.DrawDebugLine(new Vector2(-200,0), new Vector2(200,0), 4, Color.BLUE, guiCam.combined);
		batch.end();
	}

	/**
	 * This function will be executed to dispose large resources.
	 */
	@Override
	public void dispose(){
		shader.dispose();
		modelBatch.dispose();
		batch.dispose();
		body.getJointArray().clear();
		body.getLimbArray().clear();
		assets.dispose();
		font.dispose();
	}

	@Override public void resize(int width, int height){}
	@Override public void pause(){}
	@Override public void resume(){}
}